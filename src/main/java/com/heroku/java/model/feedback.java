/*package com.heroku.java.model;


public class feedback {
	private int feedbackId;
	private String name;
	private String email;
	private String phone;
	private String star;
	private String comments;
	    	
	    	public feedback() {
	    	}
			public void setFeedbackId(int feedbackId) {
				this.feedbackId = feedbackId;
			}
			public int getFeedbackId() {
				return feedbackId;
			}
	    	public String getName() {
	    		return name;
	    	}
	    	public void setName(String name) {
	    		this.name = name;
	    	}
	    	public String getEmail() {
	    		return email;
	    	}
	    	public void setEmail(String email) {
	    		this.email = email;
	    	}
	    	public String getPhone() {
	    		return phone;
	    	}
	    	public void setPhone(String phone) {
	    		this.phone = phone;
	    	}
	    	public String getStar() {
	    		return star;
	    	}
	    	public void setStar(String star) {
	    		this.star = star;
	    	}
	    	public String getComments() {
	    		return comments;
	    	}
	    	public void setComments(String comments) {
	    		this.comments = comments;
	    	}
          
	    	
} */


package com.heroku.java.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;
    private String name;
    private String email;

    // Getters and setters
    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

