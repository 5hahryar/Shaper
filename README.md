# Shaper
Shaper is a simple cloud-based Todo app

![alt text](https://github.com/5hahryar/Shaper/blob/master/ShaperShot.png?raw=true)

## Setup your own Firebase project
Shaper uses Firebase Authentication and Firestore to maintain user data.

### Getting Started
- [Add Firebase to your Android Project](https://firebase.google.com/docs/android/setup).
- Note: you don't need to edit project's gradle files as they are already ready to go.

### Google Sign In Setup

- Go to the Firebase Console and navigate to your project:
  - Select the **Auth** panel and then click the **Sign In Method** tab.
  - Click **Google** and turn on the **Enable** switch, then click **Save**.
    
### Cloud Firestore Security Rules

Add the following security rules to your project in the:
[rules tab](https://console.firebase.google.com/project/_/database/firestore/rules):

```
service cloud.firestore {  
  service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow write: if request.auth != null;
      allow read: if request.auth.uid == userId;
      	match /tasks/{documents=**} {
        	allow write, read: if request.auth != null && request.auth.uid == userId;
        }
    }
  }
}
```

## Support
Reach out to me at shahryar.kh78@gmail.com

Feel free to make any contributions.
