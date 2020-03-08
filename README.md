android-utils  
[![](https://jitpack.io/v/github2136/Android-utils.svg)](https://jitpack.io/#github2136/Android-utils)  
使用该库还需要引用以下库  
**implementation 'androidx.appcompat:appcompat:1.0.2'**  
**implementation 'androidx.recyclerview:recyclerview:1.0.0'**  
**implementation 'com.google.code.gson:gson:2.8.5'**

一些常用工具类  
**BitmapUtil** 图片压缩、旋转、缩略图处理，参考**BitmapActivity**  
**CommonUtil** DP转PX/PX转DP，通知权限开启判断，Intent是否可执行（调用系统Intent可使用）  
**DateUtil** 日期处理Date转String/String转Date，传入日期返回与当前时间差 DateActivity  
**FileUtil** 文件处理类判定是否有外部存储读写权限、返回存储根目录等其他路径，根据前缀、后缀生成文件名，获取文件大小，根据路径获取文件后缀，保存文件至指定目录。**可以在Manifest中添加路径（util_project_path）**  
**JsonUtil** json处理类，使用Gson，参考**Appli**  
**SpanUtil** SpannableStringBuilder文字处理类  
**SPUtil** SharedPreferences处理类。**可以在Manifest中添加SP名（util_sp_name）**  
**PermissionUtil** 6.0动态权限请求类  
**NetworkUtil** 网络状态判定  
**CrashHandler** 异常捕获类，默认把异常信息存储外部存储目录下的Log目录下  
**AsymmetricEncryptionUtil** 非对称加密类  
**SymmetricEncryptionUtil** 对称加密类  
**MessageDigestUtil** 信息摘要类

**base** 目录中是一些常用的基础类  
**BaseListAdapter** ListView使用的BaseAdapter  
**BaseRecyclerAdapter** RecyclerView使用的BaseAdapter  
**Divider**带分组的分割线