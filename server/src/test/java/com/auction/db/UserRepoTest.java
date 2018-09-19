package com.auction.db;

import com.auction.db.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepoTest {
    @Autowired
    UserRepo userRepo;

    @Test
    public void saveUser() {
        User xuerongUser = new User("Xuerong");
        xuerongUser = userRepo.save(xuerongUser);

    }

    @Test
    public void findUser() {
        User xuerongUser = new User("Xuerong");
        xuerongUser = userRepo.save(xuerongUser);

        User savedUser = userRepo.getOne(xuerongUser.getId());
        assertThat(savedUser).isNotEqualTo(null);
        assertThat(savedUser.getId()).isEqualTo(xuerongUser.getId());
        assertThat(savedUser.getName()).isEqualTo(xuerongUser.getName());
    }

    @Test
    public void removeUser() {
        User xuerongUser = new User("Xuerong");
        xuerongUser = userRepo.save(xuerongUser);

        userRepo.delete(xuerongUser);
    }
}
