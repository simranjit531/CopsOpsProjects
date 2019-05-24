package copops.com.copopsApp.utils;

public class ChatSingleton {
        private static ChatSingleton instance = null;
        private ChatSingleton() {
            // Exists only to defeat instantiation.


        }

        public static ChatSingleton getInstance() {
            if(instance == null) {
                instance = new ChatSingleton();
            }
            return instance;
        }
}