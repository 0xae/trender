package com.dk.trender.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.dk.trender.api.*;
import com.dk.trender.core.Post;

@RegisterMapper(TimelineDAO.ListingTrendMapper.class)
public interface TimelineDAO {
	@SqlQuery(
		"select l.id, l.title,l.last_activity,count(1) as total_posts "+
		"from z_listing l join z_post p on l.id = p.listing_id "+
		"where p.time between :start and :end or (p.time::date = now()::date and p.timming='Just Now') "+
		"group by l.id order by 4 desc "+
		"limit 5 "
	)
	List<ListingTrend> getListingTrends(@Bind("start") final DateTime start,
										@Bind("end") final DateTime end);
	
	public static class ListingTrendMapper implements ResultSetMapper<ListingTrend> {
		@Override
		public ListingTrend map(int index, ResultSet r, StatementContext context) throws SQLException {
			final ListingTrend t = new ListingTrend();
			t.setId(r.getLong("id"));
			t.setName(r.getString("title"));
			t.setTotalCount(r.getLong("total_posts"));
			t.setLastActivity(new DateTime(r.getString("last_activity").replace(' ', 'T')));
			return t;
		}
	}
}
