package work.lpssfxy.www.campuslifeassistantclient.base.eventbus;

public class Events {

    public static Events events;
    // Event used to send message from fragment to activity.
    public static class FragmentActivityMessage {
        private String message;
        public FragmentActivityMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
 
    // Event used to send message from activity to fragment.
    public static class ActivityFragmentMessage {
        private String message;
        public ActivityFragmentMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
 
    // Event used to send message from activity to activity.
    public static class ActivityActivityMessage {
        private String message;
        public ActivityActivityMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    // Event used to send message from fragment to fragment.
    public static class FragmentFragmentMessage {
        private String message;
        public FragmentFragmentMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
