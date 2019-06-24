package pages.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import pages.pojo.User;

import java.util.List;

/**
 * @Description: 分页功能用到
 * @Author: sunyuanyuan
 * @CreateDate: 2019/6/4
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
@Repository
public interface UserDao {

    /**
     * @param userId
     * @return User
     */
    public User selectUserById(Integer userId);

    List<User> selectUserByUserName(@Param("userName") String userName);

}
