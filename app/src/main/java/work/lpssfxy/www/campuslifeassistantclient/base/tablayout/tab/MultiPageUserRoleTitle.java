package work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab;

/**
 * created by on 2021/11/27
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-11-27-21:44
 */
public enum MultiPageUserRoleTitle {

    专属角色(0),
    专属用户(1);

    private final int position;

    MultiPageUserRoleTitle(int pos) {
        position = pos;
    }

    public static MultiPageUserRoleTitle getPage(int position) {
        return MultiPageUserRoleTitle.values()[position];
    }

    public static int size() {
        return MultiPageUserRoleTitle.values().length;
    }

    public static String[] getPageNames() {
        MultiPageUserRoleTitle[] pages = MultiPageUserRoleTitle.values();
        String[] pageNames = new String[pages.length];
        for (int i = 0; i < pages.length; i++) {
            pageNames[i] = pages[i].name();
        }
        return pageNames;
    }

    public int getPosition() {
        return position;
    }
}

