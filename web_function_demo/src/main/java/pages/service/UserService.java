package pages.service;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import pages.pojo.User;

/**
 * @Description:
 * @Author: sunyuanyuan
 * @CreateDate: 2019/6/4
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
public interface UserService {
    User selectUserById(Integer userId);

    PageInfo<User> queryByPage(String userName, Integer pageNo, Integer pageSize);

}
