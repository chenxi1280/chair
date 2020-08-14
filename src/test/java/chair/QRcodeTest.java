package chair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpic.evolution.chair.ChairApplication;


/**
 * 	需求描述：
 * 	前台制作页面，点击发布后，会生成二维码，用微信扫码后可以进入我们的小程序并进行当前制作视频的播放
 *
 * 	前台会传过来videoId，此请求也应验证登录状态
 *
 * 	做法：先按照wxGetQrcode方法，使用appid和secretId拼出请求access_token
 * access_token过期时间3600秒，将此数据存入redis，过期时间3000秒，key就是access_token，若以后redis有值，优先从redis中取
 *
 * 	拿到access_token后，如下例test2，access_toke一定要放在url里，page和scene放在参数里，其中scene就是前台传过来的videoId
 *
 * 	之后判断请求成功还是失败，失败的话是返回json对象，要解析好返回前台；成功的话，直接返回我们二进制的图片，我们要把这个图片转存成
 * 	base64位图片，再发给前台
 *
 *	 以下我写的例子中，缺少第一个接口和图片的64位转换，不过主流程存二维码是通的，你可以参考一下
 *
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChairApplication.class)
public class QRcodeTest {
	
	@Test
	public void test() {
		
    }

}
