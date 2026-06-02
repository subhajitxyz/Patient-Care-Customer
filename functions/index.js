const { onDocumentUpdated } = require("firebase-functions/v2/firestore");
const admin = require("firebase-admin");

admin.initializeApp();

exports.sendHealthAlertNotification = onDocumentUpdated(
  {
    document: "patients/{patientId}/health_info/today",
    region: "asia-south1",
  },
  async (event) => {

    const before = event.data.before.data();
    const after = event.data.after.data();
    const patientId = event.params.patientId;

    if (!after) return;

    const healthTypes = ["heart_attack", "extreme_breath", "extreme_cough"];

    for (const type of healthTypes) {

      const beforeState = before?.[type];
      const afterState = after?.[type];

      if (!afterState) continue;
      if (afterState.seen !== true) continue;

      // ✅ Detect if the field object changed
      if (JSON.stringify(beforeState) !== JSON.stringify(afterState)) {

        const accuracy = afterState.accuracy || "0";
        const time = afterState.time?.toDate()?.getTime().toString() || "";

        const notification = buildNotification(type, accuracy);

        const userDoc = await admin
          .firestore()
          .collection("patients")
          .doc(patientId)
          .collection("basic_info")
          .doc("profile")
          .get();

        if (!userDoc.exists) continue;

        const tokens = userDoc.data().fcmToken || [];

        if (!Array.isArray(tokens) || tokens.length === 0) {
          continue;
        }

        const response = await admin.messaging().sendEachForMulticast({
          tokens,

          android: {
            priority: "high",
          },

          data: {
            title: notification.title,
            body: notification.body,
            type: "health_alert",
            healthType: type,
            accuracy: String(accuracy),
            time: String(time),
          },
        });

        // Remove invalid tokens
        const invalidTokens = [];

        response.responses.forEach((resp, idx) => {
          if (!resp.success) {

            const errorCode = resp.error?.code;

            if (
              errorCode ===
                "messaging/registration-token-not-registered" ||
              errorCode ===
                "messaging/invalid-registration-token"
            ) {
              invalidTokens.push(tokens[idx]);
            }
          }
        });

        // Cleanup invalid tokens
        if (invalidTokens.length > 0) {

          await admin
            .firestore()
            .collection("patients")
            .doc(patientId)
            .collection("basic_info")
            .doc("profile")
            .update({
              fcmToken:
                admin.firestore.FieldValue.arrayRemove(
                  ...invalidTokens
                ),
            });
        }
      }
    }
  }
);

function buildNotification(type, accuracy) {
  switch (type) {

    case "heart_attack":
      return {
        title: "💔 Possible Heart Attack Detected",
        body: `Possible heart attack detected. Confidence: ${accuracy}%`,
      };

    case "extreme_breath":
      return {
        title: "🫁 Possible Breathing Problem Detected",
        body: `Extreme breathing pattern detected. Confidence: ${accuracy}%`,
      };

    case "extreme_cough":
      return {
        title: "🚨 Possible Severe Cough Detected",
        body: `Frequent coughing detected. Confidence: ${accuracy}%`,
      };

    default:
      return {
        title: "Health Alert",
        body: "Abnormal health activity detected",
      };
  }
}