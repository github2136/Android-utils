android-utils  
[![](https://jitpack.io/v/github2136/Android-utils.svg)](https://jitpack.io/#github2136/Android-utils)  
使用该库还需要引用以下库  
**implementation 'com.google.code.gson:gson:2.8.2'**

一些常用工具类  
**BitmapUtil** 图片压缩、旋转、缩略图处理 BitmapActivity  
**CollectionsUtil** 集合非空、空判断  
**CommonUtil** DP转PX/PX转DP，通知权限开启判断，Intent是否可执行（调用系统Intent可使用）  
**DateUtil** 日期处理Date转String/String转Date，传入日期返回与当前时间差 DateActivity  
**FileUtil** 文件处理类判定是否有外部存储读写权限、返回存储根目录等其他路径，根据前缀、后缀生成文件名，获取文件大小，根据路径获取文件后缀，保存文件至指定目录。**必须在Manifest中添加默认路径（util_project_path）**  
**JsonUtil** json处理类，使用Gson  
**SpanUtil** SpannableStringBuilder文字处理类  
**SPUtil** SharedPreferences处理类。**必须在Manifest中添加默认名（util_sp_name）**  

**base** 目录中是一些常用的基础类  
**BaseListAdapter** ListView使用的BaseAdapter  
**BaseRecyclerAdapter** RecyclerView使用的BaseAdapter  
**BaseLoadMoreRecyclerAdapter** 带上拉更多的BaseAdapter，具体使用方法参考LoadMoreActivity

