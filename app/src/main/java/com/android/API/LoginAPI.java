package com.android.API;

public class LoginAPI {
    /*Socket mSocket;
    Emitter.Listener onNewImage;

    public LoginAPI() {
    }

    public void loginUser(FirebaseUser firebaseUser) {
        User user = new User(firebaseUser.getUid(),firebaseUser.get)
    }

    private void handleNewImage(final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String data = (String) arg;
                if (data.contains("unsuccess")) {
                    Log.d(TAG, "handleNewImage: unsuccess");
                } else {
                    user.setImage(data);
                    Log.d(TAG, "handleNewImage: success: "+data);
                }
            }
        });
    }
    private void initSocket() {
        mSocket.connect();
        mSocket.on(SERVER_SEND_IMAGE, onNewImage);
    }

    private void sendImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.abcde);
        bitmap = GlobalFunction.resize(bitmap, 100, 100);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        mSocket.emit(CLIENT_SEND_IMAGE, GlobalFunction.getByteArrayFromBitmap(bitmap),json);
    }


    {

        try {
            mSocket = IO.socket(GlobalStaticData.URL_HOST);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        onNewImage = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                handleNewImage(args[0]);
            }
        };
    }*/


}
