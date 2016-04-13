# ADBlocker
You can use this in your webview to filter annoying ads.

从AdBlockplus的项目中摘出了一部分代码，再进行一些自己的实现，就是这个项目了。
源项目在这里：https://github.com/adblockplus/adblockplusandroid

AdBlockplus是通过设置wifi代理的方式，将手机全局的代理设置到本机的2020端口，然后在2020端口进行广告的过滤。
其广告过滤的规则使用的是社区维护的规则表，不同的地区有不同的规则表

本项目去掉了AdBlockplus中的代理部分，只保留了核心的功能————根据规则表来判断url是否应该被过滤。
将这个功能添加到浏览器中（正如demo中的实现一样），就可以实现类似于猎豹浏览器的广告过滤效果


下面是三个可以使用的规则地址（过滤强度依次减弱）：
https://easylist-downloads.adblockplus.org/easylistchina+easylist.txt
https://easylist-downloads.adblockplus.org/easylistchina.txt
https://adfiltering-rules.googlecode.com/svn/trunk/lastest/rules_for_ABP.txt

源项目是符合GPL的开源协议的，所以我这个也只能GPL了，有需要的童鞋自己解决协议的问题吧。。。
