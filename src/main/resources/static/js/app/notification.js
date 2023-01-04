const firebaseModule = (function () {
    async function init() {
        // Your web app's Firebase configuration
        if ('serviceWorker' in navigator) {
            window.addEventListener('load', function() {
                navigator.serviceWorker.register('/firebase-messaging-sw.js')
                    .then(registration => {
                        var firebaseConfig = {
                              apiKey: "AIzaSyBpT8NxVl85PnQNjaFWoNCiUD8wsW4m81E",
                              authDomain: "webpush-coin.firebaseapp.com",
                              projectId: "webpush-coin",
                              storageBucket: "webpush-coin.appspot.com",
                              messagingSenderId: "824680102690",
                              appId: "1:824680102690:web:5897e0c6bf6ceb4513437b",
                              measurementId: "G-GFQBMDM8YW"
                        };

                        // Initialize Firebase
                        console.log("firebase Initialization");
                        firebase.initializeApp(firebaseConfig);

                        // Show Notificaiton Dialog
                        const messaging = firebase.messaging();
                        messaging.requestPermission()
                        .then(function() {
                            console.log("Permission granted to get token");
                            return messaging.getToken();
                        })
                        .then(async function(token) {
                            console.log("Token: ", token);
                            await fetch('/register', { method: 'post', body: token })
                            messaging.onMessage(payload => {
                                const title = payload.notification.title
                                const options = {
                                    body : payload.notification.body
                                }
                                navigator.serviceWorker.ready.then(registration => {
                                    registration.showNotification(title, options);
                                })
                            })
                        })
                        .catch(function(err) {
                            console.log("Error Occured : " +err );
                        })
                    })
            })
        }
    }

    return {
        init: function () {
            init()
        }
    }
})()

firebaseModule.init()