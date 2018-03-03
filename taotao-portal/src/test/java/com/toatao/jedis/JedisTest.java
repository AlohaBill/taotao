package com.toatao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	/**
	 * 单机版
	 */
	@Test
	public void testSingle() {
		// 创建一个jedis对象
		Jedis jedis = new Jedis("192.168.146.128", 6379);
		// 调用对象的方法
		jedis.set("mamami", "测试成功!");
		String str = jedis.get("mamami");
		System.out.println(str);
		jedis.close();
	}

	/**
	 * 数据库连接池
	 */
	@Test
	public void testJedisPool() {
		// 创建连接池
		JedisPool pool = new JedisPool("192.168.146.128", 6379);
		// 从连接池中取得jedis对象
		Jedis jedis = pool.getResource();
		jedis.set("key1", "test here1212");
		String str = jedis.get("key1");
		System.out.println(str);
		jedis.close();
		pool.close();
	}

	/**
	 * 集群
	 */
	@Test
	public void testJedisCluster() {
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.146.128", 7001));
		nodes.add(new HostAndPort("192.168.146.128", 7002));
		nodes.add(new HostAndPort("192.168.146.128", 7003));
		nodes.add(new HostAndPort("192.168.146.128", 7004));
		nodes.add(new HostAndPort("192.168.146.128", 7005));
		nodes.add(new HostAndPort("192.168.146.128", 7006));
		JedisCluster cluster = new JedisCluster(nodes);
		cluster.set("mimi", "咪咪");
		System.out.println(cluster.get("mimi"));
		cluster.close();
	}
	/**
	 * spring整合jedis单机版测试
	 */
	@Test
	public void testSpringJedisSingle() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");
		JedisPool pool = (JedisPool) applicationContext.getBean("redisClient");
		Jedis jedis = pool.getResource();
		String string = jedis.get("key1");
		System.out.println(string);
		jedis.close();
		pool.close();
	}
	/**
	 * spring整合jedis集群版测试
	 */
	@Test
	public void testSpringJedisCluster() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-*.xml");
		JedisCluster jedisCluster = (JedisCluster) applicationContext.getBean("redisClient");
		String string = jedisCluster.get("key1");
		System.out.println(string);
		jedisCluster.close();
	}

}