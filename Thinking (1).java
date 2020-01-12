package ThreadUploadDemo;
/**
 *
 *
 * 			 run			������                                            eventCache.pop() ��ѯ�Ƿ��л����Event
 * acceptor ----> accept()---->  socket ----nioChannels.pop()----      -------
 *(Acceptor)			                       �Ƿ��л����NioCHannel  | 	 	     no	 |	    | yes
 * 													                   |              	\|/    \|/
 * 										     no	 |	    | yes          |
 * 												\|/	   \|/             |                	\|/    \|/
 * 													                   |             	new	   reset(socket,interest)
 * 													                   |         	        ---------
				                            	new	   reset()         |
 * 													  |                |          	        ---------
 * 													  |		           |                        |����event���еȴ�����
 * 												����poller��           |                       \|/
 * 											poller.register()---------		                      events.offer(event);--->wakeUpCount.getAndIncrement()--->selector.wakeup()
 *
 * 					|------------------------------------------------------------------------------------------
 * 					|																						|
 * 			run	   \|/																						|
 * poller -----> event()  --------------------------------wakeupCount.getAndSet(-1)>1    ----				|
 * (Poller)					|									true |			|false								|
 * 					|								   		\|/	   	   \|/									|
 * 				   \|/									selectNow()   select(TimeOut)						|
 * 				events.poll() ȡ��event						-------------									|
 * 					|		                                      |											|
 * 				   \|/ 											  |											|
 * 				event.run()  ����event	ע��interestֵ			 \|/										|
 * 					|																						|
 * 				   \|/											keyCount>0------|							|
 * 				 event.reset() �¼�����Ĭ��ֵ ����			true	|			|false						|
 * 					|											   \|/			|							|
 * 				   \|/				     						iterator()		|-------------------------------
 * 				eventsCache.push(event) ���� �ѱ�����					|  �õ�key
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
 * 													SocketProcessor sc = processorCache.pop();//�ӻ����еõ�SocketProcessor
 * 																	|
 * 																   \|/sc == null
 * 																---------
 * 															true|		|false
 * 															   \|/     \|/
 * 															new  		reset  ������eventCahce����
 * 																---------
 * 																	|
 * 																   \|/
 * 																sc.run() ��ʽ��ʼ����ô�����
 * 																	|
 * 																   \|/
 * 																 sc.reset() //�ָ�Ĭ�� �Ա���������
 * 																	|
 * 																   \|/
 * 																processorCache.push(sc);
 *
 *Acceptor��·��Ҫ�ǽ��ܿͻ������Ӳ������ӽ��з�װ��PollerEvent�¼�ģʽ�����ҷ���ȴ��¼�����.
 *��Ҫ����Acceptor.accept() NioEndpoint.setSocketOptions() Poller.register() Poller.events()


 Poller��·��Ҫ�����ǲ���ѯ�ʵȴ��¼��������Ƿ��еȴ������¼�,����������event()��������ִ��PollerEvent.run�������÷�����Ҫ�Ա���event��socketChannel��ָ��selector�Ͻ���ע�����Ȥ�¼���
Poller�߳�select�����������¼���Ȼ�����processSocket���д���

 * @author zhengzechao
 *
 */
public class Thinking
{

}
