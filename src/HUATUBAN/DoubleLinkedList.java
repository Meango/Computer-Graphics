package HUATUBAN;
/**
 * 双向链表类，用于界面的初始化和部分动作的监听和处理
 * 
 * @author Yaomz
 * @version 1.0
 */
class Point{
	public int x;
	public int y;
	
	public Point(int x,int y){
		this.x=x;
		this.y=y;
	}
}
//定义双向链表节点
class Node{
  Point data;
  Node next;   //后一个节点
  Node prev;   //前一个节点
  int IsIntersection; //标记是否为交点，1表示交点，0表示顶点 
	int IsBeTracked;  //标记是否为跟踪过，1表示被跟踪过，0表示未被跟踪过 ，默认为0 
	
  //构造函数
  public Node(){
  	data=null;
  	prev=null;
  	next=null;
  	IsIntersection=0;
  	IsBeTracked=0;
  }
  public Node(Point data,int IsIntersection,int IsBeTracked){
  	this.data=data;
  	this.prev=null;
  	this.next=null;
  	this.IsIntersection=IsIntersection;
  	this.IsBeTracked=IsBeTracked;
  }
  public Node(Point data,Node prev,Node next,int IsIntersection,int IsBeTracked){
  	this.data=data;
  	this.prev=prev;
  	this.next=next;
  	this.IsIntersection=IsIntersection;
  	this.IsBeTracked=IsBeTracked;
  }
}
public class DoubleLinkedList{
	
	public int size=0;
	public Node beginMarker;
	public Node endMarker;
	
	//初始化一个空的双向循环链表
	public DoubleLinkedList(){
		beginMarker=new Node();
		endMarker=new Node();
		//key point
		beginMarker.prev=endMarker;
		beginMarker.next=null;
		endMarker.prev=null;
		//together
		endMarker.next=beginMarker;
	}
	
	public void init(){
		//System.out.println("双向循环链表的操作：");
		//System.out.println("1.空的双向循环链表已经建立");
	}
	
	
	// add 方法
	public void add(Point data,int IsIntersection,int IsBeTracked){
		if(beginMarker.next==null){
			Node node=new Node(data,IsIntersection,IsBeTracked);
			beginMarker.next=node;
			node.prev=beginMarker;
			endMarker=node;
			endMarker.next=beginMarker;
		} 
		else{
			Node node=new Node(data,IsIntersection,IsBeTracked);
			endMarker.next=node;
			node.prev=endMarker;
			endMarker=node;
			endMarker.next=beginMarker;
		}
	}
	
	//在index位置上插入data，index从0开始
	public void add(int index,Point data,int IsIntersection,int IsBeTracked){
		int j=0;
		if (beginMarker==null){
			Node node=new Node(data,IsIntersection,IsBeTracked);
			beginMarker.next=node;
			node.prev=beginMarker;
			endMarker=node;
			endMarker.next=beginMarker;
		}
		else if(index==0){
			Node node=new Node(data,IsIntersection,IsBeTracked);
			Node temp=beginMarker.next;
			beginMarker.next=node;
			node.prev=beginMarker;
			node.next=temp;
			temp.prev=node;
		} 
		else if(index>=size()){
			add(data,IsIntersection,IsBeTracked);
		} else 
		{
			Node node=new Node(data,IsIntersection,IsBeTracked);
			Node prior=beginMarker;
			while (j<index)
			{
				j++;
				prior=prior.next;
			}
			Node temp=prior.next;
			prior.next=node;
			node.prev=prior;
			node.next=temp;
			temp.prev=node;
		}
	}
	
	//删除index位置上的数据，index从0开始
	public void remove(int index) {
		int j=0;
		int n=1;
		for(int i=0;i<n;i++){
		    if (index<0||index>=size()) {
		    	System.out.println("数组越界");
			} 
		    else if(index==0||size()==1) {
		    	if (size()==0){
		    		beginMarker.next=null;
		    		endMarker=null;
		    	} 
		    	else {
		    		Node fitst=beginMarker.next;
		    		beginMarker.next=fitst.next;
		    		fitst=null;
		    	}
		    } 
		    else if(index==(size()-1)){
		    	if(size()==1){
		    		if (size()==0){
		    			beginMarker.next=null;
		    			endMarker=null;
		    		} 
		    		else {
		    			Node fitst=beginMarker.next;
		    			beginMarker.next=fitst.next;
		    			fitst=null;
		    		}
		    	} 
		    	else{
		    		Node pre=endMarker.prev;
		    		pre.next=null;
		    		endMarker=pre;
		    		endMarker.next=beginMarker;
					endMarker=null;
		    	}
		    } 
		    else {
		    	Node prior=beginMarker.next;
		    	while(j<index){
		    		j++;
		    		prior=prior.next;
		    	}
		    	Node delete=prior;
		    	Node pre=delete.prev;
		    	Node after=delete.next;
		    	pre.next=delete.next;
		    	after.prev=pre;
		    	delete=null;
		    }
		}
	}
	
	//用于计算链表的大小
	public int size(){ 
		int size=0;
		Node node=beginMarker.next;
		while(node.data!=null) {
			size++;
			node=node.next;
		}
		return size;
	}
	
	//用于得到index位置上的节点
	public Node getNode(int index) {
		int j=0;
		Node firstNode=beginMarker.next;
		if(index<0||index>=size()){
			System.out.println("数组越界");
		}
		while(j<index){
			j++;
			firstNode=firstNode.next;
		}
		return firstNode;
	} 
	
	//用于得到index位置上的节点
	public Node getNode(Point data) {
		Node firstNode=beginMarker.next;
		while(firstNode.data.x!=data.x||firstNode.data.y!=data.y){
			firstNode=firstNode.next;
		}
		return firstNode;
	} 
	
	//用于得到index位置上的节点
	public int getLocation(Point data) {
		Node firstNode=beginMarker.next;
		int j=0;
		while(firstNode.data.x!=data.x||firstNode.data.y!=data.y){
			firstNode=firstNode.next;
			j++;
		}
		return j;
	} 

	//实现链表的逆置操作
	public void inverse(DoubleLinkedList list1){
		int size=size();
		for(int i=size-1;i>=0;i--){
			list1.add(this.getNode(i).data,this.getNode(i).IsIntersection,this.getNode(i).IsBeTracked);
		}
	}
	
	//该方法用于输出链表中的所有数值
	public void print(){
		Node firstNode=beginMarker.next;
		if(firstNode==null){
			System.out.println("该链表为空");
		} 
		else{
			while(firstNode.data!=null){
				System.out.println(firstNode.data.x+" "+firstNode.data.y+" "+firstNode.IsIntersection+" "+firstNode.IsBeTracked);
				firstNode=firstNode.next;
			}
		}
	}
	
	
}
