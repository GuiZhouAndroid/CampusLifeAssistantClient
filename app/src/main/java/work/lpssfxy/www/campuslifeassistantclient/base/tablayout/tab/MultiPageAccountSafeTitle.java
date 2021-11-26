package work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab;

/**
 * created by on 2021/11/26
 * 描述：开发者后台中心，账户封禁安全TabLayout标题枚举类
 *
 * @author ZSAndroid
 * @create 2021-11-26-17:20
 */
public enum MultiPageAccountSafeTitle {

    踢人下线姓名(0),
    踢人下线Token(1),
    封禁账户(2),
    封禁状态(3),
    封禁时间(4),
    账户解封(5);
    private final int position;

    MultiPageAccountSafeTitle(int pos) {
        position = pos;
    }

    public static MultiPageAccountSafeTitle getPage(int position) {
        return MultiPageAccountSafeTitle.values()[position];
    }

    public static int size() {
        return MultiPageAccountSafeTitle.values().length;
    }

    public static String[] getPageNames() {
        MultiPageAccountSafeTitle[] pages = MultiPageAccountSafeTitle.values();
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
