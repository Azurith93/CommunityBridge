package org.communitybridge.dao;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.communitybridge.main.Configuration;
import org.communitybridge.main.SQL;
import org.communitybridge.utility.Log;
import org.communitybridge.utility.StringUtilities;

public class KeyValueWebGroupDao extends WebGroupDao
{
	public static final String EXCEPTION_MESSAGE_GETSECONDARY = "Exception during KeyValueWebGroupDao.getSecondaryGroups(): ";

	public KeyValueWebGroupDao(Configuration configuration, SQL sql, Log log)
	{
		super(configuration, sql, log);
	}

	@Override
	public void addGroup(String userID, String groupID, int currentGroupCount) throws IllegalAccessException, InstantiationException, MalformedURLException, SQLException
	{
		if (currentGroupCount > 0)
		{
			groupID = configuration.webappSecondaryGroupGroupIDDelimiter + groupID;
		}
		String query = "UPDATE `" + configuration.webappSecondaryGroupTable + "` "
								 + "SET `" + configuration.webappSecondaryGroupGroupIDColumn + "` = CONCAT(`" + configuration.webappSecondaryGroupGroupIDColumn + "`, '" + groupID + "') "
								 + "WHERE `" + configuration.webappSecondaryGroupUserIDColumn + "` = '" + userID + "' "
								 + "AND `" + configuration.webappSecondaryGroupKeyColumn + "` = '" + configuration.webappSecondaryGroupKeyName + "' ";
		sql.updateQuery(query);
	}

	@Override
	public void removeGroup(String userID, String groupID) throws IllegalAccessException, InstantiationException, MalformedURLException, SQLException
	{
		String query = "SELECT `" + configuration.webappSecondaryGroupGroupIDColumn + "` "
								 + "FROM `" + configuration.webappSecondaryGroupTable + "` "
								 + "WHERE `" + configuration.webappSecondaryGroupUserIDColumn + "` = '" + userID + "' "
								 + "AND `" + configuration.webappSecondaryGroupKeyColumn + "` = '" + configuration.webappSecondaryGroupKeyName + "' ";
		result = sql.sqlQuery(query);
		
		if (result.next())
		{
			String groupIDs = result.getString(configuration.webappSecondaryGroupGroupIDColumn);
			List<String> groupIDsAsList = new ArrayList<String>(Arrays.asList(groupIDs.split(configuration.webappSecondaryGroupGroupIDDelimiter)));
			groupIDsAsList.remove(groupID);
			groupIDs = StringUtilities.joinStrings(groupIDsAsList, configuration.webappSecondaryGroupGroupIDDelimiter);
			query = "UPDATE `" + configuration.webappSecondaryGroupTable + "` "
						+ "SET `" + configuration.webappSecondaryGroupGroupIDColumn + "` = '" + groupIDs + "' "
						+ "WHERE `" + configuration.webappSecondaryGroupUserIDColumn + "` = '" + userID + "' "
						+ "AND `" + configuration.webappSecondaryGroupKeyColumn + "` = '" + configuration.webappSecondaryGroupKeyName + "'";
			sql.updateQuery(query);
		}		
	}
	
	@Override
	public List<String> getUserSecondaryGroupIDs(String userID) throws MalformedURLException, InstantiationException, IllegalAccessException, SQLException
	{
		List<String> groupIDs = new ArrayList<String>();
		String query =
						"SELECT `" + configuration.webappSecondaryGroupGroupIDColumn + "` "
					+ "FROM `" + configuration.webappSecondaryGroupTable + "` "
					+ "WHERE `" + configuration.webappSecondaryGroupUserIDColumn + "` = '" + userID + "' "
					+ "AND `" + configuration.webappSecondaryGroupKeyColumn + "` = '" + configuration.webappSecondaryGroupKeyName + "' ";

		result = sql.sqlQuery(query);

		if (result.next())
		{
			return convertDelimitedIDString(result.getString(configuration.webappSecondaryGroupGroupIDColumn));
		}
		return groupIDs;
	}

	@Override
	public List<String> getGroupUserIDs(String groupID)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<String> getGroupUserIDsPrimary(String groupID)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<String> getGroupUserIDsSecondary(String groupID)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
