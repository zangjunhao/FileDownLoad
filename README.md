# FileDownLoad
```
红岩作业，主要考察service的使用和多线程吧
因为时间安排不当导致这周有个比赛，结果就剩一天做作业
全部就是在第一行代码的基础上添加了多线程下载、断点续传的功能
只学会了AsyncTask和服务的用法，最基础的那种消息处理还是不会
```
- .setThreadNumber方法设置线程数，默认为线程数*2(默认核心数其实可以防止并行问题
- .getProgressUi方法设置具有进度条功能的ui
- 断点续传没有用sql所以其实只能续传第一个线程的(逃
- 然后就是.write和.notify实现的暂停功能
- ![image](https://github.com/zangjunhao/FileDownLoad/blob/master/app/666.gif)
