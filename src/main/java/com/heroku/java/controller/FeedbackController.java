/*package com.heroku.java.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.heroku.java.model.feedback;

@Controller
public class FeedbackController {
    private final DataSource dataSource;

    @Autowired
    public FeedbackController(DataSource dataSource) {
        this.dataSource = dataSource;
    }


@PostMapping("/addFeedback")
public String addFeedback(@ModelAttribute("addFeedback") feedback feedback) {
try (Connection connection = dataSource.getConnection()) {
	String sql = "INSERT INTO public.feedback(name,email) VALUES(?,?)";
	final var statement = connection.prepareStatement(sql);

	
		String name = feedback.getName();
		String email = feedback.getEmail();

		statement.setString(1, name);
		statement.setString(2, email);

		statement.executeUpdate();
		connection.close();

}catch (Exception e) {
	e.printStackTrace();
	//return "error";
  }
  return "redirect:/homepage";

}




@GetMapping("/FeedbackList")
    public String FeedbackList(Model model) {
        List<feedback> feedbacks = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT  feedbackid, name, email FROM public.feedback ORDER BY feedbackid";
            final var statement = connection.prepareStatement(sql);
            final var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int feedbackId = resultSet.getInt("feedbackid");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");

                feedback feedback = new feedback();
                feedback.setFeedbackId(feedbackId);
                feedback.setName(name);
                feedback.setEmail(email);

                feedbacks.add(feedback);
            }

            model.addAttribute("feedbacks", feedbacks);

        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }

        return "homepage/";
    }

}
 */


//  package com.heroku.java.controller;

//  import java.sql.Connection;
//  import java.sql.SQLException;
//  import java.util.ArrayList;
//  import java.util.List;
 
//  import javax.sql.DataSource;
 
//  import org.springframework.beans.factory.annotation.Autowired;
//  import org.springframework.stereotype.Controller;
//  import org.springframework.ui.Model;
//  import org.springframework.web.bind.annotation.GetMapping;
//  import org.springframework.web.bind.annotation.ModelAttribute;
//  import org.springframework.web.bind.annotation.PostMapping;
 
//  import com.heroku.java.model.feedback;
 
//  @Controller
//  public class FeedbackController {
//      private final DataSource dataSource;
 
//      @Autowired
//      public FeedbackController(DataSource dataSource) {
//          this.dataSource = dataSource;
//      }
 
//      @PostMapping("/addFeedback")
//      public String addFeedback(@ModelAttribute("addFeedback") feedback feedback) {
//          try (Connection connection = dataSource.getConnection()) {
//              String sql = "INSERT INTO public.feedback(name, email) VALUES(?, ?)";
//              final var statement = connection.prepareStatement(sql);
 
//              statement.setString(1, feedback.getName());
//              statement.setString(2, feedback.getEmail());
 
//              statement.executeUpdate();
//          } catch (Exception e) {
//              e.printStackTrace();
//              return "error";
//          }
//          return "redirect:/homepage";
//      }
 
//      @GetMapping("/FeedbackList")
//      public String FeedbackList(Model model) {
//          List<feedback> feedbacks = new ArrayList<>();
 
//          try (Connection connection = dataSource.getConnection()) {
//              String sql = "SELECT feedbackid, name, email FROM public.feedback ORDER BY feedbackid";
//              final var statement = connection.prepareStatement(sql);
//              final var resultSet = statement.executeQuery();
 
//              while (resultSet.next()) {
//                  feedback feedback = new feedback();
//                  feedback.setFeedbackId(resultSet.getLong("feedbackid"));
//                  feedback.setName(resultSet.getString("name"));
//                  feedback.setEmail(resultSet.getString("email"));
 
//                  feedbacks.add(feedback);
//              }
 
//              model.addAttribute("feedbacks", feedbacks);
 
//          } catch (SQLException e) {
//              e.printStackTrace();
//              return "error";
//          }
 
//          return "feedbackList";
//      }
//  }
 