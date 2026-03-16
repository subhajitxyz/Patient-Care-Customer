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

        if (!userDoc.exists) return;

        const token = userDoc.data().fcmToken;
        if (!token) return;

        await admin.messaging().send({
          token,
          android: { priority: "high" },
          data: {
            title: notification.title,
            body: notification.body,
            type: "health_alert",
            healthType: type,
            accuracy: String(accuracy),
            time: String(time),
          },
        });

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