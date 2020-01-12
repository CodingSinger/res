package ThreadUploadDemo;
/**
 *
 *
 * 			 run			有连接                                            eventCache.pop() 查询是否有缓存的Event
 * acceptor ----> accept()---->  socket ----nioChannels.pop()----      -------
 *(Acceptor)			                       是否有缓存的NioCHannel  | 	 	     no	 |	    | yes
 * 													                   |              	\|/    \|/
 * 										     no	 |	    | yes          |
 * 												\|/	   \|/             |                	\|/    \|/
 * 													                   |             	new	   reset(socket,interest)
 * 													                   |         	        ---------
				                            	new	   reset()         |
 * 													  |                |          	        ---------
 * 													  |		           |                        |放入event队列等待处理
 * 												放入poller中           |                       \|/
 * 											poller.register()---------		                      events.offer(event);--->wakeUpCount.getAndIncrement()--->selector.wakeup()
 *
 * 					|------------------------------------------------------------------------------------------
 * 					|																						|
 * 			run	   \|/																						|
 * poller -----> event()  --------------------------------wakeupCount.getAndSet(-1)>1    ----				|
 * (Poller)					|									true |			|false								|
 * 					|								   		\|/	   	   \|/									|
 * 				   \|/									selectNow()   select(TimeOut)						|
 * 				events.poll() 取出event						-------------									|
 * 					|		                                      |											|
 * 				   \|/ 											  |											|
 * 				event.run()  运行event	注册interest值			 \|/										|
 * 					|																						|
 * 				   \|/											keyCount>0------|							|
 * 				 event.reset() 事件重置默认值 加入			true	|			|false						|
 * 					|											   \|/			|							|
 * 				   \|/				     						iterator()		|-------------------------------
 * 				eventsCache.push(event) 缓存 已备后用					|  得到key
 * 																   \|/
 * 																processKey(key)
 * 																	|
 * 																   \|/
 * 																socket = key.channel()
 * 																	|
 * 																   \|/
 * 																processSocket(socket)
 * 																	|
 * 																   \|/
 * 													SocketProcessor sc = processorCache.pop();//从缓存中得到SocketProcessor
 * 																	|
 * 																   \|/sc == null
 * 																---------
 * 															true|		|false
 * 															   \|/     \|/
 * 															new  		reset  类似于eventCahce缓存
 * 																---------
 * 																	|
 * 																   \|/
 * 																sc.run() 正式开始处理该次请求
 * 																	|
 * 																   \|/
 * 																 sc.reset() //恢复默认 以备缓存再用
 * 																	|
 * 																   \|/
 * 																processorCache.push(sc);
 *
 *Acceptor线路主要是接受客户端连接并对连接进行封装成PollerEvent事件模式，并且放入等待事件队列.
 *重要方法Acceptor.accept() NioEndpoint.setSocketOptions() Poller.register() Poller.events()


 Poller线路主要任务是不断询问等待事件队列中是否有等待处理事件,如果有则调用event()方法进行执行PollerEvent.run方法，该方法主要对本次event的socketChannel在指定selector上进行注册感兴趣事件。
Poller线程select出被触发的事件，然后调用processSocket进行处理。

 * @author zhengzechao
 *
 */
public class Thinking
{

}
