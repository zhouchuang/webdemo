package user.zc.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import user.zc.utils.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class GroupTypeFormAuthenticationFilter extends FormAuthenticationFilter {

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {

		String username = this.getUsername(request);
		String password = this.getPassword(request);
		return new UsernamePasswordToken(username, password);
	}
	//获取选择的区域语言
	private String getLocalelang(ServletRequest request){
		String lang =  WebUtils.getCleanParam(request, "lang");
		if(StringUtils.isEmpty(lang)){
			lang = "zh_CN";
		}
		return   lang;
	}


}
