package work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab;

/**
 * created by on 2021/12/1
 * 描述：开发者后台中心，认证管理TabLayout标题枚举类
 *
 * @author ZSAndroid
 * @create 2021-12-01-22:54
 */
public enum MultiPageCerRunTitle {

    全部详情(0);

    private final int position;

    MultiPageCerRunTitle(int pos) {
        position = pos;
    }

    public static MultiPageCerRunTitle getPage(int position) {
        return MultiPageCerRunTitle.values()[position];
    }

    public static int size() {
        return MultiPageCerRunTitle.values().length;
    }

    public static String[] getPageNames() {
        MultiPageCerRunTitle[] pages = MultiPageCerRunTitle.values();
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
