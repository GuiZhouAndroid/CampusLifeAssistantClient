package work.lpssfxy.www.campuslifeassistantclient.base;/**
 * created by on 2021/11/5
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-11-05-20:16
 */
public class XPopup {
    // 显示从底部弹出的列表弹窗：这种弹窗从 1.0.0版本开始实现了优雅的手势交互和智能嵌套滚动
//                                            new XPopup.Builder(LoginActivity.this)
//                                                    .asBottomList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5"},
//                                                            new OnSelectListener() {
//                                                                @Override
//                                                                public void onSelect(int position, String text) {
//                                                                    toast("click " + text);
//                                                                }
//                                                            })
//                                                    .show();
    //显示确认和取消对话框
//                                            new XPopup.Builder(LoginActivity.this).asConfirm("我是标题", "我是内容",
//                                                    new OnConfirmListener() {
//                                                        @Override
//                                                        public void onConfirm() {
//                                                            toast("click confirm");
//                                                        }
//                                                    })
//                                                    .show();

    //显示中间弹出的列表弹窗
//                                            new XPopup.Builder(LoginActivity.this)
//                                                    //.maxWidth(600)
//                                                    .asCenterList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4"},
//                                                            new OnSelectListener() {
//                                                                @Override
//                                                                public void onSelect(int position, String text) {
//                                                                    toast("click " + text);
//                                                                }
//                                                            })
//                                                    .show();
    //显示带输入框的确认和取消对话框
//                                            new XPopup.Builder(LoginActivity.this).asInputConfirm("我是标题", "请输入内容。",
//                                                    new OnInputConfirmListener() {
//                                                        @Override
//                                                        public void onConfirm(String text) {
//                                                            toast("input text: " + text);
//                                                        }
//                                                    })
//                                                    .show();
    //显示中间弹出的加载框——***代替我原有的***
//                                            new XPopup.Builder(LoginActivity.this)
//                                                    .asLoading("正在加载中")
//                                                    .show();


    //显示依附于某个View或者某个点的弹窗
//                                            new XPopup.Builder(LoginActivity.this)
//                                                    .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
//                                                    .asAttachList(new String[]{"分享", "编辑", "不带icon"},
//                                                            new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher},
//                                                            new OnSelectListener() {
//                                                                @Override
//                                                                public void onSelect(int position, String text) {
//                                                                    toast("click " + text);
//                                                                }
//                                                            })
//                                                    .show();
//
//                                            // 必须在事件发生前，调用这个方法来监视View的触摸
//                                            final XPopup.Builder builder = new XPopup.Builder(LoginActivity.this)
//                                                    .watchView(view.findViewById(R.id.btnShowAttachPoint));
//                                            view.setOnLongClickListener(new View.OnLongClickListener() {
//                                                @Override
//                                                public boolean onLongClick(View v) {
//                                                    builder.asAttachList(new String[]{"置顶", "复制", "删除"}, null,
//                                                            new OnSelectListener() {
//                                                                @Override
//                                                                public void onSelect(int position, String text) {
//                                                                    toast("click " + text);
//                                                                }
//                                                            })
//                                                            .show();
//                                                    return false;
//                                                }
//                                            });
    //大图浏览弹窗
    //当你点击图片的时候执行以下代码：
    // 多图片场景（你有多张图片需要浏览）
    //srcView参数表示你点击的那个ImageView，动画从它开始，结束时回到它的位置。
//                                            new XPopup.Builder(getContext()).asImageViewer(imageView, position, list, new OnSrcViewUpdateListener() {
//                                                @Override
//                                                public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
//                                                    // 注意这里：根据position找到你的itemView。根据你的itemView找到你的ImageView。
//                                                    // Demo中RecyclerView里面只有ImageView所以这样写，不要原样COPY。
//                                                    // 作用是当Pager切换了图片，需要更新源View，
//                                                    popupView.updateSrcView((ImageView) recyclerView.getChildAt(position));
//                                                }
//                                            }, new SmartGlideImageLoader())
//                                                    .show();
//
//                                            // 单张图片场景
//                                            new XPopup.Builder(getContext())
//                                                    .asImageViewer(imageView, url, new SmartGlideImageLoader())
//                                                    .show();

//                                            BasePopupView popupView = new XPopup.Builder(LoginActivity.this)
//                                                    .asLoading("正在加载中")
//                                                    .show();
//                                            //有三个消失方法可供选择：
//                                            //popupView.dismiss();  //立即消失
//                                            popupView.delayDismiss(300);//延时消失，有时候消失过快体验可能不好，可以延时一下
    //popupView.smartDismiss(); //会等待弹窗的开始动画执行完毕再进行消失，可以防止接口调用过快导致的动画不完整。
}
