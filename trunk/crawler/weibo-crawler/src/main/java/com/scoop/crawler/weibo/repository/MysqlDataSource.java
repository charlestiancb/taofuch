package com.scoop.crawler.weibo.repository;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl;

import com.scoop.crawler.weibo.entity.OneWeiboInfo;
import com.scoop.crawler.weibo.entity.WeiboComment;
import com.scoop.crawler.weibo.entity.WeiboPersonInfo;
import com.scoop.crawler.weibo.parser.Parser;
import com.scoop.crawler.weibo.repository.entity.FetchType;
import com.scoop.crawler.weibo.repository.mysql.Comment;
import com.scoop.crawler.weibo.repository.mysql.EntityTransfer;
import com.scoop.crawler.weibo.repository.mysql.FailedRequest;
import com.scoop.crawler.weibo.repository.mysql.Fans;
import com.scoop.crawler.weibo.repository.mysql.FetchInfo;
import com.scoop.crawler.weibo.repository.mysql.Follow;
import com.scoop.crawler.weibo.repository.mysql.User;
import com.scoop.crawler.weibo.repository.mysql.Weibo;
import com.scoop.crawler.weibo.util.ClassUtils;

public class MysqlDataSource implements DataSource {
	private SessionFactory sessionFactory;

	/**
	 * 用于操作Mysql的数据源方式！
	 */
	@SuppressWarnings("deprecation")
	public MysqlDataSource() {
		if (sessionFactory == null) {
			// Hibernate的基本配置
			Properties pro = new Properties();
			pro.put(Environment.URL, "jdbc:mysql://localhost:3306/weibo?useUnicode=true&characterEncoding=UTF-8");
			pro.put(Environment.USER, "root");
			pro.put(Environment.PASS, "root");

			pro.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
			pro.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
			pro.put(Environment.CONNECTION_PROVIDER, DriverManagerConnectionProviderImpl.class.getName());

			// 使用c3p0连接池
			pro.put(Environment.C3P0_MIN_SIZE, "5");
			pro.put(Environment.C3P0_MAX_SIZE, "30");
			pro.put(Environment.C3P0_TIMEOUT, "60");// 1分钟
			pro.put(Environment.C3P0_MAX_STATEMENTS, "50");
			pro.put(Environment.C3P0_IDLE_TEST_PERIOD, "60");
			pro.put(Environment.C3P0_ACQUIRE_INCREMENT, "2");

			pro.put(Environment.AUTO_CLOSE_SESSION, "true");
			// 初始化Hibernate
			Configuration c = new Configuration();
			c.setProperties(pro);
			// 将实体类加载到Hibernate容器中！
			Set<Class<?>> clazzs = ClassUtils.getClasses(EntityTransfer.class.getPackage());
			if (clazzs != null && !clazzs.isEmpty()) {
				for (Class<?> clazz : clazzs) {
					if (Serializable.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
						c = c.addAnnotatedClass(clazz);
					}
				}
			}
			// 字段自动转换与数据库保持一致
			c.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
			// 创建SessionFactory！
			sessionFactory = c.buildSessionFactory();
		}
	}

	public void saveWeibo(OneWeiboInfo weibo) {
		// 保存微博信息，先判断存不存在，如果不存在才插入！
		try {
			if (!isWeiboExists(weibo.getId())) {
				Session s = getCurrentSession();
				Transaction t = s.beginTransaction();
				try {
					s.save(EntityTransfer.parseWeibo(weibo));
				} catch (Exception e) {
				}
				t.commit();
			}
		} catch (Exception e) {
		}
		saveFetchIfNeccessory(weibo.getId(), FetchType.weibo);
		saveUserIfNeccessory(weibo.getPublisher());
	}

	/**
	 * 保存抓取信息
	 * 
	 * @param id
	 * @param weibo
	 */
	private void saveFetchIfNeccessory(String id, FetchType weibo) {
		try {
			FetchInfo fi = new FetchInfo(Parser.getQuery(), id, weibo.name());
			if (fi.needSave()) {
				Session s = getCurrentSession();
				Object obj = s.createQuery("from FetchInfo where queryStr=? and relationId=? and relationType=?")
								.setString(0, fi.getQueryStr())
								.setString(1, fi.getRelationId())
								.setString(2, fi.getRelationType())
								.uniqueResult();
				if (obj == null) {
					Transaction t = s.beginTransaction();
					try {
						s.save(fi);
					} catch (Exception e) {
					}
					t.commit();
				}
			}
		} catch (Exception e) {
		}
	}

	public void close() {
		// sessionFactory.close();
	}

	public void saveComment(WeiboComment comment) {
		// 保存微博评论信息，先判断存不存在，如果不存在才插入！
		try {
			if (!isCommentExists(comment.getId())) {
				Session s = getCurrentSession();
				Transaction t = s.beginTransaction();
				try {
					s.save(EntityTransfer.parseComment(comment));
				} catch (Exception e) {
				}
				t.commit();
			}
		} catch (Exception e) {
		}
		savePerson(comment.getPerson());
	}

	public void savePerson(WeiboPersonInfo person) {
		// 保存发布者信息，即用户信息，先判断存不存在，如果不存在才插入！
		saveUserIfNeccessory(person);
		saveFans(person.getId(), person.getFans());
		saveFollows(person.getId(), person.getFollows());
	}

	/**
	 * 保存粉丝信息
	 * 
	 * @param fans
	 */
	protected void saveFans(String userId, List<WeiboPersonInfo> fans) {
		userId = StringUtils.trim(userId);
		if (StringUtils.isNotEmpty(userId) && fans != null && fans.size() > 0) {
			for (WeiboPersonInfo fansUser : fans) {
				try {
					Fans f = new Fans();
					f.setUserId(userId);
					f.setFansId(fansUser.getId());
					Session s = getCurrentSession();
					Transaction t = s.beginTransaction();
					try {
						s.save(f);
					} catch (Exception e) {
					}
					t.commit();
				} catch (Exception e) {
				}
				saveUserIfNeccessory(fansUser);
			}
		}
	}

	/**
	 * 保存关注信息
	 * 
	 * @param follows
	 */
	protected void saveFollows(String userId, List<WeiboPersonInfo> follows) {
		userId = StringUtils.trim(userId);
		if (StringUtils.isNotEmpty(userId) && follows != null && follows.size() > 0) {
			for (WeiboPersonInfo followUser : follows) {
				try {
					Follow f = new Follow();
					f.setUserId(userId);
					f.setFollowId(followUser.getId());
					Session s = getCurrentSession();
					Transaction t = s.beginTransaction();
					try {
						s.save(f);
					} catch (Exception e) {
					}
					t.commit();
				} catch (Exception e) {
				}
				saveUserIfNeccessory(followUser);
			}
		}
	}

	/**
	 * 如果用户不存在，则保存！
	 * 
	 * @param person
	 */
	protected void saveUserIfNeccessory(WeiboPersonInfo person) {
		try {
			if (!isUserExists(person.getId())) {
				// 如果用户不存在，则保存！
				Session s = getCurrentSession();
				Transaction t = s.beginTransaction();
				try {
					s.save(EntityTransfer.parseUser(person));
				} catch (Exception e) {
				}
				t.commit();
			}
		} catch (Exception e) {
		}
	}

	protected Session getCurrentSession() {
		return sessionFactory.openSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public boolean isWeiboExists(String weiboId) {
		boolean r = false;
		try {
			r = getCurrentSession().get(Weibo.class, weiboId) != null;
		} catch (Exception e) {
		}
		return r;
	}

	public boolean isCommentExists(String commentId) {
		boolean r = false;
		try {
			r = getCurrentSession().get(Comment.class, commentId) != null;
		} catch (Exception e) {
		}
		return r;
	}

	public boolean isUserExists(String userId) {
		boolean r = false;
		try {
			r = getCurrentSession().get(User.class, userId) != null;
		} catch (Exception e) {
		}
		return r;
	}

	public static void main(String[] args) {
		Weibo weibo = new Weibo();
		weibo.setCommentNum(2L);
		weibo.setContent("这是测试内容哦~");
		weibo.setForwordNum(3L);
		weibo.setOrign("手工");
		weibo.setPublishTime("2012-11-27");
		weibo.setUrl("http://adfasdfasd");
		weibo.setUserId("adfXaADSZxa-adax");
		weibo.setWeiboId("asdfasdfDSdjgh");
		Session s = new MysqlDataSource().getCurrentSession();
		Transaction t = s.beginTransaction();
		s.save(weibo);
		t.commit();
	}

	public void saveFailedRequest(FailedRequest request) {
		// 保存失败的请求！
		try {
			Session s = getCurrentSession();
			Transaction t = s.beginTransaction();
			try {
				s.save(request);
			} catch (Exception e) {
			}
			t.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FailedRequest pop() {
		FailedRequest req = (FailedRequest) getCurrentSession().createCriteria(FailedRequest.class)
																.setFetchSize(1)
																.uniqueResult();
		if (req != null && req.getRecId() != null) {
			try {
				Session s = getCurrentSession();
				Transaction tx = s.beginTransaction();
				try {
					s.delete(req);
				} catch (Exception e) {
				}
				tx.commit();
			} catch (Exception e) {
			}
		}
		return req;
	}
}
