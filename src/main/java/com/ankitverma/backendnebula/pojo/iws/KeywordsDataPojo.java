/**
 * 
 */
package careers.nebula.ben.pojo.iws;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import careers.nebula.ben.db.enitity.User;

/**
 * @author Ankit_Nebula
 *
 */
@XmlRootElement
public class KeywordsDataPojo {
	private Integer id;
	private String name;
	private Collection<User> userList = new ArrayList<User>();
	
	public KeywordsDataPojo(){
		
	}
	
	public KeywordsDataPojo(Integer id, String name, Collection<User> userList){
		this.id = id;
		this.name = name;
		this.userList = userList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<User> getUserList() {
		return userList;
	}

	public void setUserList(Collection<User> userList) {
		this.userList = userList;
	}
}
