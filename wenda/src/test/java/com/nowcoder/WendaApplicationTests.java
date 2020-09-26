package com.nowcoder;

import com.nowcoder.dao.QuestionDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import com.nowcoder.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.List;
import java.util.Random;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@WebAppConfiguration
public class WendaApplicationTests {

    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    FollowService followService;

    @Test
    public void initDatabase() {
//        Random random = new Random();
//        for (int i = 0; i < 11; i++) {
//            User user = new User();
//            user.setName(String.valueOf(i));
//            user.setPassword("");
//            user.setSalt("");
//            user.setHeadUrl("http://images.nowcoder.com/head/" + random.nextInt(1000) + "t.png");
//            userDAO.addUser(user);
//            user.setPassword("xx");
//            userDAO.updatePassword(user);
//        }
//        User user = userDAO.selectById(35);
//        System.out.println(user);

//        for (int i = 0; i < 11; i++) {
//            Question question = new Question();
//            question.setCommentCount(i);
//            Date date = new Date();
//            date.setTime(date.getTime() + 1000 * 3600 * i);
//            question.setCreatedDate(date);
//            question.setUserId(i);
//            question.setTitle("TITLE_" + i);
//            question.setContent("Balabalabala_Content_" + i);
//            questionDAO.addQuestion(question);
//        }

//        List<Question> questionList = questionDAO.selectLatestQuestions(0, 0, 10);
//        System.out.println();

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == j) {
                    continue;
                }
                followService.follow(j, EntityType.ENTITY_USER, i);
            }
        }

    }

}
