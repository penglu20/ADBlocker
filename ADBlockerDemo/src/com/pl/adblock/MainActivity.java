package com.pl.adblock;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pl.adblocker.ADBlocker;
import com.pl.adblocker.util.ADBlockerPatterns;


public class MainActivity extends Activity {
	Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 10001:
				Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
				break;
			case 10000:				
				Toast.makeText(getApplicationContext(), "开始下载", Toast.LENGTH_SHORT).show();
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	private TextView mTextView;
	private Button mButton;
	private Button mtestV8JsEngineButton;	
	private String[] urlsString={"http://m.dddbbb.net/favicon.ico"
			,"http://m.dddbbb.net/"
			,"http://hm.baidu.com/hm.gif?cc=0&ck=1&cl=32-bit&ds=720x1280&ep=%5B%7Bx%3A164%2Cy%3A6%2Ct%3Ab%7D%5D&et=2&ja=1&ln=zh-CN&lo=0&lt=1444119013&nv=0&rnd=1574536204&si=b9f1dadf1c5d7744a3d6866ecdd7bce7&st=4&v=1.1.8&lv=2"
			,"http://hm.baidu.com/hm.gif?cc=0&ck=1&cl=32-bit&ds=720x1280&ep=19292%2C19295&et=3&ja=1&ln=zh-CN&lo=0&lt=1444119013&nv=0&rnd=1230856504&si=b9f1dadf1c5d7744a3d6866ecdd7bce7&st=4&v=1.1.8&lv=2"
			,"http://m.dddbbb.net/index.css"
			,"http://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"
			,"http://m.dddbbb.net/img/TOPBtn.gif"
			,"http://b.ddfate.com/cpro/ui/dm.js"
			,"http://pos.baidu.com/sccm?di=u2228540&dri=0&dis=0&dai=1&ps=175x0&coa=&dcb=BAIDU_DUP_Mobile_define&dtm=BAIDU_DUP2_SETJSONADSLOT&dvi=0.0&dci=-1&dds=&dpt=none&tsr=61&tpr=1444124345257&dbv=2&drs=1&pcs=360x519&pss=360x175&cfv=0&cpl=0&chi=22&cce=true&cec=GBK&tlm=1444095545&ti=%B6%B9%B6%B9%D0%A1%CB%B5%D4%C4%B6%C1%CD%F8%20-%20%CC%EC%CC%EC%B8%FC%D0%C2%B5%C4%C3%E2%B7%D1%D1%D4%C7%E9%D0%A1%CB%B5&ltu=http%3A%2F%2Fm.dddbbb.net%2F&liu=&ltr=&lcr=&psr=720x1280&par=720x1280&pis=-1x-1&ccd=32&cja=true&cmi=0&col=zh-CN&cdo=0&tcn=1444124345&baidu_id="
			,"http://p.tanx.com/ex?i=mm_28110653_2418111_27396914&m=1"
			,"http://pos.baidu.com/wclm?di=u2228542&dri=0&dis=0&dai=2&ps=2008x0&coa=&dcb=BAIDU_DUP_Mobile_define&dtm=BAIDU_DUP2_SETJSONADSLOT&dvi=0.0&dci=-1&dds=&dpt=none&tsr=150&tpr=1444124345257&dbv=2&drs=1&pcs=360x519&pss=360x2008&cfv=0&cpl=0&chi=22&cce=true&cec=GBK&tlm=1444095545&ti=%B6%B9%B6%B9%D0%A1%CB%B5%D4%C4%B6%C1%CD%F8%20-%20%CC%EC%CC%EC%B8%FC%D0%C2%B5%C4%C3%E2%B7%D1%D1%D4%C7%E9%D0%A1%CB%B5&ltu=http%3A%2F%2Fm.dddbbb.net%2F&liu=&ltr=&lcr=&psr=720x1280&par=720x1280&pis=-1x-1&ccd=32&cja=true&cmi=0&col=zh-CN&cdo=0&tcn=1444124345&baidu_id="
			,"http://pos.baidu.com/sccm?di=u2228543&dri=0&dis=0&dai=3&ps=2967x0&coa=&dcb=BAIDU_DUP_Mobile_define&dtm=BAIDU_DUP2_SETJSONADSLOT&dvi=0.0&dci=-1&dds=&dpt=none&tsr=195&tpr=1444124345257&dbv=2&drs=1&pcs=360x519&pss=360x2967&cfv=0&cpl=0&chi=22&cce=true&cec=GBK&tlm=1444095545&ti=%B6%B9%B6%B9%D0%A1%CB%B5%D4%C4%B6%C1%CD%F8%20-%20%CC%EC%CC%EC%B8%FC%D0%C2%B5%C4%C3%E2%B7%D1%D1%D4%C7%E9%D0%A1%CB%B5&ltu=http%3A%2F%2Fm.dddbbb.net%2F&liu=&ltr=&lcr=&psr=720x1280&par=720x1280&pis=-1x-1&ccd=32&cja=true&cmi=0&col=zh-CN&cdo=0&tcn=1444124345&baidu_id="
			,"http://pos.baidu.com/vchm?di=u2187902&dri=0&dis=0&dai=4&ps=3207x0&coa=&dcb=BAIDU_DUP_Mobile_define&dtm=BAIDU_DUP2_SETJSONADSLOT&dvi=0.0&dci=-1&dds=&dpt=none&tsr=211&tpr=1444124345257&dbv=2&drs=1&pcs=360x519&pss=360x3207&cfv=0&cpl=0&chi=22&cce=true&cec=GBK&tlm=1444095545&ti=%B6%B9%B6%B9%D0%A1%CB%B5%D4%C4%B6%C1%CD%F8%20-%20%CC%EC%CC%EC%B8%FC%D0%C2%B5%C4%C3%E2%B7%D1%D1%D4%C7%E9%D0%A1%CB%B5&ltu=http%3A%2F%2Fm.dddbbb.net%2F&liu=&ltr=&lcr=&psr=720x1280&par=720x1280&pis=-1x-1&ccd=32&cja=true&cmi=0&col=zh-CN&cdo=0&tcn=1444124345&baidu_id="
			,"http://hm.baidu.com/h.js?b9f1dadf1c5d7744a3d6866ecdd7bce7"
			,"http://pos.baidu.com/vchm?dtm=BAIDU_DUP2_SETJSONADSLOT&dc=2&di=u2228540&rsi0=360&rsi1=90&adn=1&at=97&cad=1&ccd=32&cec=GBK&cfv=0&ch=0&col=zh-CN&conOP=0&cpa=1&dai=1&dis=0&ltu=http%3A%2F%2Fm.dddbbb.net%2F&lunum=6&n=legume_cpr&pcs=360x519&pis=10000x10000&ps=175x0&psr=720x1280&pss=360x175&qn=ca733000b897f62a&rsi5=4&scale=20.5&skin=mobile_skin_white_blue&td_id=2228540&tn=template_inlay_all_mobile&tpr=1444124345257&ts=1&wuliao_domain=a.ddfate.com&wt=1&distp=1001&conW=360&conH=90&ti=%B6%B9%B6%B9%D0%A1%CB%B5%D4%C4%B6%C1%CD%F8%20-%20%CC%EC%CC%EC%B8%FC%D0%C2%B5%C4%C3%E2%B7%D1%D1%D4%C7%E9%D0%A1%CB%B5"
			,"http://atanx.alicdn.com/t/tanxmobile/tanxssp.js"
			,"http://pos.baidu.com/vchm?dtm=BAIDU_DUP2_SETJSONADSLOT&dc=2&di=u2228542&rsi0=360&rsi1=90&adn=1&at=97&cad=1&ccd=32&cec=GBK&cfv=0&ch=0&col=zh-CN&conOP=0&cpa=1&dai=2&dis=0&ltu=http%3A%2F%2Fm.dddbbb.net%2F&lunum=6&n=legume_cpr&pcs=360x519&pis=10000x10000&ps=2008x0&psr=720x1280&pss=360x2008&qn=869c4072c1f763cc&rsi5=4&scale=20.5&skin=mobile_skin_white_blue&td_id=2228542&tn=template_inlay_all_mobile&tpr=1444124345257&ts=1&wuliao_domain=a.ddfate.com&wt=1&distp=1001&conW=360&conH=90&ti=%B6%B9%B6%B9%D0%A1%CB%B5%D4%C4%B6%C1%CD%F8%20-%20%CC%EC%CC%EC%B8%FC%D0%C2%B5%C4%C3%E2%B7%D1%D1%D4%C7%E9%D0%A1%CB%B5"
			,"http://pagead2.googlesyndication.com:443"
			,"https://googleads.g.doubleclick.net/pagead/html/r20151006/r20150820/zrt_lookup.html"
			,"http://pos.baidu.com/vchm?dtm=BAIDU_DUP2_SETJSONADSLOT&dc=2&di=u2228543&rsi0=360&rsi1=90&adn=1&at=97&cad=1&ccd=32&cec=GBK&cfv=0&ch=0&col=zh-CN&conOP=0&cpa=1&dai=3&dis=0&ltu=http%3A%2F%2Fm.dddbbb.net%2F&lunum=6&n=legume_cpr&pcs=360x519&pis=10000x10000&ps=2967x0&psr=720x1280&pss=360x2967&qn=6e9d54c7d572b195&rsi5=4&scale=20.5&skin=mobile_skin_white_blue&td_id=2228543&tn=template_inlay_all_mobile&tpr=1444124345257&ts=1&wuliao_domain=a.ddfate.com&wt=1&distp=1001&conW=360&conH=90&ti=%B6%B9%B6%B9%D0%A1%CB%B5%D4%C4%B6%C1%CD%F8%20-%20%CC%EC%CC%EC%B8%FC%D0%C2%B5%C4%C3%E2%B7%D1%D1%D4%C7%E9%D0%A1%CB%B5"
			,"http://hm.baidu.com/hm.gif?cc=0&ck=1&cl=32-bit&ds=720x1280&ep=19292%2C19295&et=3&ja=1&ln=zh-CN&lo=0&lt=1444119013&nv=0&rnd=1230856504&si=b9f1dadf1c5d7744a3d6866ecdd7bce7&st=4&v=1.1.8&lv=2&u=http%3A%2F%2Fm.dddbbb.net%2F"
			,"http://hm.baidu.com/hm.gif?cc=0&ck=1&cl=32-bit&ds=720x1280&ep=%5B%7Bx%3A164%2Cy%3A6%2Ct%3Ab%7D%5D&et=2&ja=1&ln=zh-CN&lo=0&lt=1444119013&nv=0&rnd=1574536204&si=b9f1dadf1c5d7744a3d6866ecdd7bce7&st=4&v=1.1.8&lv=2&u=http%3A%2F%2Fm.dddbbb.net%2F"
			,"http://hm.baidu.com/hm.gif?cc=0&ck=1&cl=32-bit&ds=720x1280&et=0&ja=1&ln=zh-CN&lo=0&lt=1444119013&nv=0&rnd=305234699&si=b9f1dadf1c5d7744a3d6866ecdd7bce7&st=4&v=1.1.8&lv=2"
			,"http://boscdn.bpc.baidu.com/v1/holmes-moplus/mp-cdn.html"
			,"http://googleads.g.doubleclick.net/pagead/ads?client=ca-pub-1608069659050670&format=360x100&output=html&h=100&slotname=1353970942&adk=1661203262&w=360&lmt=1444095545&flash=0&url=http%3A%2F%2Fm.dddbbb.net%2F&resp_fmts=3&wgl=1&dt=1444124345508&bpp=58&bdt=571&shv=r20151006&cbv=r20150820&saldr=aa&correlator=2431385192449&frm=20&ga_vid=779499076.1444124346&ga_sid=1444124346&ga_hid=1128674592&ga_fc=0&pv=2&u_tz=480&u_his=22&u_java=1&u_h=1280&u_w=720&u_ah=1280&u_aw=720&u_cd=32&u_nplug=0&u_nmime=0&dff=sans-serif&dfs=13&adx=0&ady=92&biw=360&bih=519&eid=317150304&oid=3&rx=0&eae=0&fc=126&pc=1&brdim=0%2C0%2C0%2C0%2C720%2C0%2C720%2C1038%2C360%2C519&vis=1&rsz=%7Cd%7Co%7C&abl=NS&ppjl=u&pfx=0&srr=1&fu=136&bc=1&ifi=1&xpc=UoOAfVv3Lb&p=http%3A//m.dddbbb.net&dtd=368"
			,"http://wn.pos.baidu.com/adx.php?c=d25pZD1lMTU2ZTRhMGEwYjIxNDJiAHM9ZTE1NmU0YTBhMGIyMTQyYgB0PTE0NDQzODM1NDQAc2U9MQBidT0xAHByaWNlPVZoZUxPQUFQQ2pCN2pFcGdXNUlBOHRsNTN6M0ppR2xTNHN2cy1RAGNoYXJnZV9wcmljZT1WaGVMT0FBUENqQjdqRXBnVzVJQTh0bDUzejNKaUdsUzRzdnMtUQBzaGFyaW5nX3ByaWNlPVZoZUxPQUFQQ2pCN2pFcGdXNUlBOHRsNTN6M0ppR2xTNHN2cy1RAHdpbl9kc3A9MQBjaG1kPTEAYmRpZD05MkFEMDFGOEEzMUVFRjkwRjI2MTMzNDRBMjQ1QkY3RABjcHJvaWQ9OTJBRDAxRjhBMzFFRUY5MEYyNjEzMzQ0QTI0NUJGN0QAYmNobWQ9MAB2PTEAaT1kNTZhZjU3OQ"
			,"http://cpro2.baidustatic.com/cpro/ui/noexpire/js/3.1.5/cpro.js"
			,"http://127.0.0.1:6259/getcuid?callback=getCuid"
			,"http://cpro.baidustatic.com/cpro/expire/time2.js"
			,"http://wn.pos.baidu.com/adx.php?c=d25pZD02ZDk5ZjY0ZWRjZTViNDRmAHM9NmQ5OWY2NGVkY2U1YjQ0ZgB0PTE0NDQzODM1NDUAc2U9MQBidT0xAHByaWNlPVZoZUxPUUFCY3FoN2pFcGdXNUlBOG9EZkxXVnBVajVaeDZ2d293AGNoYXJnZV9wcmljZT1WaGVMT1FBQmNxaDdqRXBnVzVJQThvRGZMV1ZwVWo1Wng2dndvdwBzaGFyaW5nX3ByaWNlPVZoZUxPUUFCY3FoN2pFcGdXNUlBOG9EZkxXVnBVajVaeDZ2d293AHdpbl9kc3A9MQBjaG1kPTEAYmRpZD05MkFEMDFGOEEzMUVFRjkwRjI2MTMzNDRBMjQ1QkY3RABjcHJvaWQ9OTJBRDAxRjhBMzFFRUY5MEYyNjEzMzQ0QTI0NUJGN0QAYmNobWQ9MAB2PTEAaT02NDI4NWUzZA"
			,"http://static1.searchbox.baidu.com/static/searchbox/openjs/mp.js"
			,"http://wn.pos.baidu.com/adx.php?c=d25pZD1mNGQ5MTFmNGFmYWQxZWU4AHM9ZjRkOTExZjRhZmFkMWVlOAB0PTE0NDQzODM1NDUAc2U9MQBidT0xAHByaWNlPVZoZUxPUUFBX2doN2pFcGdXNUlBOG14aHJ2ajVEdzkzSDRGX1h3AGNoYXJnZV9wcmljZT1WaGVMT1FBQV9naDdqRXBnVzVJQThteGhydmo1RHc5M0g0Rl9YdwBzaGFyaW5nX3ByaWNlPVZoZUxPUUFBX2doN2pFcGdXNUlBOG14aHJ2ajVEdzkzSDRGX1h3AHdpbl9kc3A9MQBjaG1kPTEAYmRpZD05MkFEMDFGOEEzMUVFRjkwRjI2MTMzNDRBMjQ1QkY3RABjcHJvaWQ9OTJBRDAxRjhBMzFFRUY5MEYyNjEzMzQ0QTI0NUJGN0QAYmNobWQ9MAB2PTEAaT0xNzUxMDIzMw"
			,"http://fonts.googleapis.com/css?family=Slabo+27px:400&lang=zh-CN"
			,"http://static.googleadsserving.cn/pagead/images/nessie_icon_tiamat_white.png"
			,"http://static.googleadsserving.cn/pagead/js/r20151006/r20110914/abg.js"
			,"http://googleads.g.doubleclick.net/pagead/images/mtad/back_blue.png"
			,"http://googleads.g.doubleclick.net/pagead/images/mtad/abg_blue.png"
			,"http://googleads.g.doubleclick.net/pagead/images/mtad/x_blue.png"
			,"http://cm.g.doubleclick.net/push?client=ca-pub-1608069659050670&srn=gdn"
			,"http://fonts.gstatic.com/s/slabo27px/v3/PuwvqkdbcqU-fCZ9Ed-b7b3hpw3pgy2gAi-Ip7WPMi0.woff"
			,"http://static.googleadsserving.cn/pagead/images/x_button_blue2.png"
			,"http://pagead2.googlesyndication.com/pagead/osd.js"
			,"http://weather.ksmobile.com:443"
			,"http://weather.ksmobile.com:443"
			,"http://127.0.0.1:40310/getcuid?secret=0&mcmdf=inapp_baidu_bdgjs&callback=_box_jsonp410"
			,"http://127.0.0.1:6259/getcuid?secret=0&mcmdf=inapp_baidu_bdgjs&callback=_box_jsonp411"
			,"http://weather.ksmobile.com:443"
			,"http://weather.ksmobile.com:443"
			,"http://cpro2.baidustatic.com/cpro/ui/noexpire/img/2.0.1/bd-logo4.png"
			,"http://eclick.baidu.com/time.jpg?stamp=1444124378451&s1=0&s2=0&s3=0&s4=1444124346357&s5=32094&templateid=template_inlay_all_mobile&feid=7720&s=f4d911f4afad1ee8"
			,"http://cpro2.baidustatic.com/cpro/ui/noexpire/img/2.0.1/bd-logo4.png"
			,"http://a.ddfate.com/media/v1/0f000PDTSJBGtug20fJSjf.jpg"
			,"http://pagead2.googlesyndication.com/bg/f1aHJbOOZM60Kg6U7ReoEiDxLgQAvUpfXbgqYhCii3Y.js"
			,"http://pos.baidu.com/vchm?dtm=BAIDU_DUP2_SETJSONADSLOT&dc=2&di=u2187902&rsi0=360&rsi1=54&adn=1&at=99&cad=1&ccd=32&cec=GBK&cfv=0&ch=0&col=zh-CN&conOP=0&cpa=1&dai=4&dis=0&ltu=http%3A%2F%2Fm.dddbbb.net%2F&lunum=6&n=legume_cpr&pcs=360x519&pis=10000x10000&ps=3207x0&psr=720x1280&pss=360x3207&qn=4c4ad381de879236&rsi5=4&scale=20.3&skin=mobile_skin_black_white&td_id=2187902&tn=template_inlay_all_mobile&tpr=1444124345257&ts=1&wuliao_domain=a.ddfate.com&xuanting=3&wt=1&distp=2006&conW=360&conH=54&ti=%B6%B9%B6%B9%D0%A1%CB%B5%D4%C4%B6%C1%CD%F8%20-%20%CC%EC%CC%EC%B8%FC%D0%C2%B5%C4%C3%E2%B7%D1%D1%D4%C7%E9%D0%A1%CB%B5"
			,"http://pos.baidu.com/vchm?dtm=BAIDU_DUP2_SETJSONADSLOT&dc=2&di=u2187902&rsi0=360&rsi1=54&adn=1&at=99&cad=1&ccd=32&cec=GBK&cfv=0&ch=0&col=zh-CN&conOP=0&cpa=1&dai=4&dis=0&ltu=http%3A%2F%2Fm.dddbbb.net%2F&lunum=6&n=legume_cpr&pcs=360x519&pis=10000x10000&ps=3207x0&psr=720x1280&pss=360x3207&qn=4c4ad381de879236&rsi5=4&scale=20.3&skin=mobile_skin_black_white&td_id=2187902&tn=template_inlay_all_mobile&tpr=1444124345257&ts=1&wuliao_domain=a.ddfate.com&xuanting=3&wt=1&distp=2006&conW=360&conH=54&ti=%B6%B9%B6%B9%D0%A1%CB%B5%D4%C4%B6%C1%CD%F8%20-%20%CC%EC%CC%EC%B8%FC%D0%C2%B5%C4%C3%E2%B7%D1%D1%D4%C7%E9%D0%A1%CB%B5"
			,"http://cpro.baidustatic.com/cpro/ui/noexpire/img/2.0.0/xuantingClose3.png"
			,"http://ope.tanx.com/wap?i=mm_28110653_2418111_27396914&cb=jsonp_callback_31496&callback=&userid=&o=&f=&n=&ds=720x1280&r=1&cah=1280&caw=720&ccd=32&ctz=8&chl=22&cg=adbba2376ba1d15431b99b81523c9d9e&pvid=abcd78c59ffbc625b53d5b0ccd37f2b5&ai=0&ac=1257&pro=23618557&os=android&cas=pro&cbh=3613&cbw=360&dx=1&u=http%3A%2F%2Fm.dddbbb.net%2F&osv=4.44&ori=0&dpr=2000&pf=android&k=&tt=%E8%B1%86%E8%B1%86%E5%B0%8F%E8%AF%B4%E9%98%85%E8%AF%BB%E7%BD%91%20-%20%E5%A4%A9%E5%A4%A9%E6%9B%B4%E6%96%B0%E7%9A%84%E5%85%8D%E8%B4%B9%E8%A8%80%E6%83%85%E5%B0%8F%E8%AF%B4"
			,"http://eclick.baidu.com/time.jpg?stamp=1444124378960&s1=0&s2=0&s3=0&s4=1444124346228&s5=32732&templateid=template_inlay_all_mobile&feid=7720&s=e156e4a0a0b2142b"
			,"http://cpro2.baidustatic.com/cpro/ui/noexpire/js/3.1.5/cpro.js"
			,"http://wn.pos.baidu.com/adx.php?c=d25pZD05OGRiNGVlMWYwMjg0OThhAHM9OThkYjRlZTFmMDI4NDk4YQB0PTE0NDQzODM1NzgAc2U9MQBidT0xAHByaWNlPVZoZUxXZ0FCTGV4N2pFcGdXNUlBOG5UTGdvQnlqdzlKM0hGTUhnAGNoYXJnZV9wcmljZT1WaGVMV2dBQkxleDdqRXBnVzVJQThuVExnb0J5anc5SjNIRk1IZwBzaGFyaW5nX3ByaWNlPVZoZUxXZ0FCTGV4N2pFcGdXNUlBOG5UTGdvQnlqdzlKM0hGTUhnAHdpbl9kc3A9MQBjaG1kPTEAYmRpZD05MkFEMDFGOEEzMUVFRjkwRjI2MTMzNDRBMjQ1QkY3RABjcHJvaWQ9OTJBRDAxRjhBMzFFRUY5MEYyNjEzMzQ0QTI0NUJGN0QAYmNobWQ9MAB2PTEAaT04ZWZhZDY5Yg"
			,"http://cm.g.doubleclick.net/pixel?google_nid=baidu&google_cm"
			,"http://eclick.baidu.com/time.jpg?stamp=1444124379329&s1=0&s2=0&s3=0&s4=1444124346269&s5=33060&templateid=template_inlay_all_mobile&feid=7720&s=6d99f64edce5b44f"
			,"http://127.0.0.1:6259/getcuid?callback=getCuid"
			,"http://cpro.baidustatic.com/cpro/expire/time2.js"
			,"http://df.tanx.com/spf?e=k1IgkQwgP3zVWpTFMv1oi3h3meTwe3NUs8EXyiKgdRpQlGiYQa2rCzVq6RhYWfeog7srOeRotZ8INBc0v5GayeZkN2y2%2FmiXmIQxHyWX9MT3ssnvGw5hDNdbJE1T5cjAdD1Uie2VxmAU58gIcMj8F%2B%2B%2BiPp5xUsj&u=&k=161&tanxssp_t=41390"
			,"http://cpro2.baidustatic.com/cpro/ui/noexpire/img/2.0.1/bd-logo4.png"
			,"http://strip.taobaocdn.com/L1/377/101011/2015-09/creation-263362lCkjtOMHUqu-617055.html?name=itemjump&url=http%3A%2F%2Fm.dddbbb.net%2F&iswt=1&pid=tt_28110653_2418111_27396914&refpid=tt_28110653_2418111_27396914&refpos=,n,i&adx_type=0&pvid=0a672a0f000056178b5a12a309978838_0&ps_id=abcd78c59ffbc625b53d5b0ccd37f2b5&fl=6&tanxdspv=http%3a%2f%2frdstat.tanx.com%2ftrd%3ff%3d%26k%3da09e279ad7f7a12a%26p%3dmm_28110653_2418111_27396914%26pvid%3d0a672a0f000056178b5a12a309978838%26s%3d320x100%26d%3d17534123%26t%3d1444383578&u=http%3A%2F%2Fm.dddbbb.net%2F&r=&tp=2&tsid=0a672a0f000056178b5a12a309978838"
			,"http://dsp.simba.taobao.com/feedback?bid=0a672a0f000056178b5a12a309978838&v=1&e=6&p=AQpnKg8AAFYXi1oSowmXiDjjKhtcctKJnQ%3D%3D"
			,"http://atanx.alicdn.com/t/tanxclick.js"
			,"http://m.simba.taobao.com/?name=itemjump&o=j&pid=tt_28110653_2418111_27396914&templetId=10729&url=http%3A%2F%2Fm.dddbbb.net%2F&iswt=1&refpid=tt_28110653_2418111_27396914&refpos=%2Cn%2Ci&adx_type=0&pvid=0a672a0f000056178b5a12a309978838_0&ps_id=abcd78c59ffbc625b53d5b0ccd37f2b5&fl=6&tanxdspv=http%3A%2F%2Frdstat.tanx.com%2Ftrd%3Ff%3D%26k%3Da09e279ad7f7a12a%26p%3Dmm_28110653_2418111_27396914%26pvid%3D0a672a0f000056178b5a12a309978838%26s%3D320x100%26d%3D17534123%26t%3D1444383578&u=http%3A%2F%2Fm.dddbbb.net%2F&r=&tp=2&tsid=0a672a0f000056178b5a12a309978838&count=9&p4p=jsonp0iff6dayt"
			,"http://gtms01.alicdn.com/tps/i1/TB1cvN7JXXXXXb0XVXXSctjFpXX-17-32.png"
			,"http://gtms02.alicdn.com/tps/i2/TB1R8yaJXXXXXbVXFXXSctjFpXX-17-32.png"
			,"http://gtms01.alicdn.com/tps/i1/TB1rxX3JXXXXXb8aXXXRq4iTXXX-167-147.png"
			,"http://gtms01.alicdn.com/tps/i1/TB1I3d5JXXXXXaPXXXX1PXGGXXX-29-28.png"
			,"http://gma.alicdn.com/bao/uploaded/i1/162510124768515551/TB24k8WfFXXXXaAXXXXXXXXXXXX_!!45496251-0-saturn_solar.jpg_b.jpg_.webp"
			,"http://gma.alicdn.com/bao/uploaded/i3/130640124810197953/TB23A4LfFXXXXXZXpXXXXXXXXXX_!!32883064-0-saturn_solar.jpg_b.jpg_.webp"
			,"http://gma.alicdn.com/bao/uploaded/i1/17064056421697533/TB2oyncaVXXXXbaXpXXXXXXXXXX_!!32927064-0-saturn_solar.jpg_b.jpg_.webp"
			,"http://gma.alicdn.com/bao/uploaded/i4/190100127114729999/TB2n.d0fVXXXXcqXpXXXXXXXXXX_!!51429010-0-saturn_solar.jpg_b.jpg_.webp"
			,"http://gma.alicdn.com/bao/uploaded/i3/108340127822164023/TB2YG_rfVXXXXcWXXXXXXXXXXXX_!!36820834-0-saturn_solar.jpg_b.jpg_.webp"
			,"http://gma.alicdn.com/bao/uploaded/i1/119280123321338472/TB21u17fpXXXXcYXXXXXXXXXXXX_!!42311928-0-saturn_solar.jpg_b.jpg_.webp"
			,"http://gma.alicdn.com/bao/uploaded/i4/163490122276927731/TB2PeA_fXXXXXabXpXXXXXXXXXX_!!24016349-0-saturn_solar.jpg_b.jpg_.webp"
			,"http://gma.alicdn.com/bao/uploaded/i4/11347073058160654/TB2s0LVcXXXXXX6XpXXXXXXXXXX_!!32611347-0-saturn_solar.jpg_b.jpg_.webp"
			,"http://gma.alicdn.com/bao/uploaded/i4/11920059769680997/TB2Voy8bXXXXXX1XXXXXXXXXXXX_!!99921920-0-saturn_solar.jpg_b.jpg_.webp"
			,"http://show.re.taobao.com/feature_v1.htm?feature_names=promoPrice&cb=jsonp1iff6dbb5&auction_ids=522050003365%2C522150751539%2C22271783217%2C45585119516%2C521145753848%2C521079209566%2C43906489060%2C44353189062%2C42065060039%2C522050003365%2C522150751539%2C42065060039%2C44353189062"
};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		AdblockPlus.getInstance().create(getApplication());
		mTextView=(TextView) findViewById(R.id.textview);
		mButton=(Button) findViewById(R.id.startadblocker);
		mButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				ADBlocker.getInstance().setEnabled(getApplication(), true);
				try {
//					ADBlocker.getInstance().startEngine();
					ADBlocker.getInstance().createAndStart(getApplication());					
				} catch (Throwable e) { 
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mtestV8JsEngineButton=(Button) findViewById(R.id.stopadblocker);
		mtestV8JsEngineButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ADBlocker.getInstance().stopEngine();
			}
		});
		findViewById(R.id.towebviewActivity).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent=new Intent(MainActivity.this,WebViewActivity.class);
				startActivity(intent);
			}
		});
		findViewById(R.id.testadblocker).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				StringBuilder sBuilder=new StringBuilder();
				for (int i = 0; i < urlsString.length; i++) {
					if (ADBlocker.getInstance().matches(urlsString[i], null, null, null)) {
						sBuilder.append(urlsString[i]+":").append(ADBlocker.getInstance().matches(urlsString[i], null, null, null)+";\n"); 
					}
				}
				mTextView.setText(sBuilder);
			}
		});

	}
	
			
	public void down1(View v){
			down("https://adfiltering-rules.googlecode.com/svn/trunk/lastest/rules_for_ABP.txt");			
	}
	public void down2(View v){
		down("https://easylist-downloads.adblockplus.org/easylistchina+easylist.txt");
	}
	public void down3(View v){
		down("https://easylist-downloads.adblockplus.org/easylistchina.txt");
	}
		
		
	public void down(final String url){
		
		new Thread(){
			@Override
			public void run(){
				mHandler.sendEmptyMessage(10000);
				ADBlockerPatterns.main(new String[]{getFilesDir().getAbsolutePath()+"/down.ini",getFilesDir().getAbsolutePath()+File.separator+ADBlocker.ADBLOCKER+"/patterns.ini",url,"true"});
				ADBlocker.getInstance().setSubscriptionUrl(getApplication(), url);
				mHandler.sendEmptyMessage(10001);
			}
		}.start();
	}
}
