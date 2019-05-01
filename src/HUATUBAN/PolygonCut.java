package HUATUBAN;

/**
 * 多边形裁剪类，用于监听画图版的界面
 * 
 * 
 * @author Yaomz
 * @version 1.0
 */


public class PolygonCut {
	public int judge=0;       //用于判断正在绘制裁剪多边形还是被裁剪多边形
	public static int res_length=0;   //裁剪后多边形数量
	
	//判断直线与线段是否相交 
	public int lineIntersectSide(Point A,Point B,Point C,Point D){  
	    double a=(C.y-A.y)*(A.x-B.x)-(C.x-A.x)*(A.y-B.y);    
	    double b=(D.y-A.y)*(A.x-B.x)-(D.x-A.x)*(A.y-B.y);  
	    if(a*b>0)    
	        return 0;   
	    return 1;    
	}
	
	/*如果一条线段与另一条线段所在直线相交，另一条线段与这条线段所在直线相交，则两条线段相交*/
	//判断线段与线段是否相交 
	public int sideIntersectSide(Point A,Point B,Point C,Point D){ 
	    if(lineIntersectSide(A,B,C,D)==0)    
	        return 0;    
	    if(lineIntersectSide(C,D,A,B)==0)    
	        return 0;    
	    return 1;
	}    
	
	//求两条线段的交点 
	public Point getIntersection(Point A,Point B,Point C,Point D){
		Point p=new Point(A.x,A.y);
		double a=((A.x-C.x)*(C.y-D.y)-(A.y-C.y)*(C.x-D.x));
		double b=((A.x-B.x)*(C.y-D.y)-(A.y-B.y)*(C.x-D.x));
		double t=a/b;
	 	p.x+=(B.x-A.x)*t;
	 	p.y+=(B.y-A.y)*t;
	 	return p;
	}
	
	//求两个点的距离
	public double Length_TwoPoint(Point A,Point B){
		return Math.sqrt((A.x-B.x)*(A.x-B.x)+(A.y-B.y)*(A.y-B.y));
	}
	
	//判断大小
	public int max(int a,int b) 
	{
		if(a>b)
			return a;
		else
			return b;
	}
	public int min(int a,int b) 
	{
		if(a>b)
			return b;
		else
			return a;
	}

	/*
	 通过该点做一条射线，与多边形交点个数判断是否在多边形内部，奇数则在内部，偶数则在外部 
	*/ 
	//判断点是否在多边形内部 
	/*public boolean PtInPolygon (Point p,Point a[]) { 
		int num = 0; 
		for (int i = 0; i < a.length-1; i++) { 
			Point p1=new Point(a[i].x,a[i].y); 
			Point p2=null;
			if(num==0){
				p2=new Point(a[i+1].x,a[i+1].y);
			}
			else{
				p2=new Point(a[(i+1)%num].x,a[(i+1)%num].y);
			}
			if ( p1.y == p2.y )
				continue; 
			if ( p.y < min(p1.y, p2.y) )
				continue; 
			if ( p.y >= max(p1.y, p2.y) )
				continue; 
			double x = (double)(p.y - p1.y) * (double)(p2.x - p1.x) / (double)(p2.y - p1.y) + p1.x; 
			if ( x > p.x ) 
				num++;
		} 
		return (num % 2 == 1);
	} */
	public boolean PtInPolygon (Point p,Point a[]) {
		int i,j,c=0;
		for(i=0,j=a.length-1;i<a.length;j=i++){
			int temp0,temp1;
			if(a[i].y>p.y){
				temp0=1;
			}
			else{
				temp0=0;
			}
			if(a[j].y>p.y){
				temp1=1;
			}
			else{
				temp1=0;
			}
			if((temp0!=temp1)&&(p.x<(a[j].x-a[i].x)*(p.y-a[i].y)/(a[j].y-a[i].y)+a[i].x)){
				if(c==0){
					c=1;
				}
				else{
					c=0;
				}
			}
		}
		return c==1;
	}

	/*
	判断上一个点，上一个点如果是交点且是进点，则这个点为出点，
	上一个点如果是交点且是出点，则这个点是进点，
	上一个点是顶点且在多边形外，则这个点是进点，
	上一个点是顶点且在多边形内，则这个点是出点
	*/
	//判断交点是进点还是出点 
	public boolean IsIntoPoint(Node p,Point b[]){
		Node pre=p.prev;
		if(pre.IsIntersection==0){
			return (PtInPolygon(pre.data,b)==false);
		}
		else{
			return IsIntoPoint(pre,b)==false;
		}
	}
	
	//判断进点是否被跟踪完 
	public Node Is_IntoPoint_BeTracked_Complete(DoubleLinkedList plist,Point b[]){
		Node pCur = plist.beginMarker.next;
		while (null != pCur.data){
			if(pCur.IsIntersection==1&&pCur.IsBeTracked==0&&IsIntoPoint(pCur,b))
				return pCur;
			pCur=pCur.next;
		}
		return null;
	}

	//判断出点是否被跟踪完 
	public Node Is_OutPoint_BeTracked_Complete(DoubleLinkedList plist,Point b[]){
		Node pCur = plist.beginMarker.next;
		while (null != pCur.data){
			if(pCur.IsIntersection==1&&pCur.IsBeTracked==0&&(IsIntoPoint(pCur,b)==false))
				return pCur;
			pCur=pCur.next;
		}
		return null;
	}
	
	//内裁剪
	public DoubleLinkedList[] getInternalPaint(DoubleLinkedList Da,DoubleLinkedList Db,Point b[]){
		int i=0;
		DoubleLinkedList res[]=new DoubleLinkedList[100];
		while(Is_IntoPoint_BeTracked_Complete(Da,b)!=null){
			int j=0;
			res[i]=new DoubleLinkedList();
			Node first=Is_IntoPoint_BeTracked_Complete(Da,b);
			first.IsBeTracked=1;
			res[i].add(first.data, first.IsIntersection, first.IsBeTracked);
			j++;
			int place=0;       //用于标记现在在哪个边框上跟踪点，0表示被裁剪多边形上，1表示裁剪多边形上 
			Node pcur=first.next;
			res[i].add(j, pcur.data, pcur.IsIntersection, pcur.IsBeTracked);
			pcur.IsBeTracked=1;
			j++;
			//System.out.println(first.data.x+" "+first.data.y+" "+first.IsIntersection+" "+first.IsBeTracked);
			//System.out.println(pcur.data.x+" "+pcur.data.y+" "+pcur.IsIntersection+" "+pcur.IsBeTracked);
			while((pcur.data.x!=first.data.x)||(pcur.data.y!=first.data.y)){
				if(pcur.IsIntersection==1){
					if(place==0){
						if(Db.getNode(pcur.data).prev.data!=null){
							pcur=Db.getNode(pcur.data).prev;
						}
						else{
							pcur=Db.endMarker.prev;
						}
						//System.out.println(pcur.data.x+" "+pcur.data.y+" "+pcur.IsIntersection+" "+pcur.IsBeTracked);
						res[i].add(j, pcur.data, pcur.IsIntersection, pcur.IsBeTracked);
						if(pcur.IsIntersection==1){
							Da.getNode(pcur.data).IsBeTracked=1;
						}
						j++;
						place=1;
					}
					else if(place==1){
						if(Da.getNode(pcur.data).next.data!=null){
							pcur=Da.getNode(pcur.data).next;
						}
						else{
							pcur=Da.beginMarker.next;
						}
						//System.out.println(pcur.data.x+" "+pcur.data.y+" "+pcur.IsIntersection+" "+pcur.IsBeTracked);
						res[i].add(j, pcur.data, pcur.IsIntersection, pcur.IsBeTracked);
						if(pcur.IsIntersection==1){
							Da.getNode(pcur.data).IsBeTracked=1;
						}
						j++;
						place=0;
					}
				}
				else{
					if(place==0){
						if(pcur.next.data!=null){
							pcur=pcur.next;
						}
						else{
							pcur=Da.beginMarker.next;
						}
					}
					else{
						if(pcur.prev.data!=null){
							pcur=pcur.prev;
						}
						else{
							pcur=Db.endMarker.prev;
						}
					}
					//System.out.println(pcur.data.x+" "+pcur.data.y+" "+pcur.IsIntersection+" "+pcur.IsBeTracked);
					res[i].add(j, pcur.data, pcur.IsIntersection, pcur.IsBeTracked);
					if(pcur.IsIntersection==1){
						Da.getNode(pcur.data).IsBeTracked=1;
					}
					j++;
				}
			}
			i++;
		}
		res_length=i;
		return res;
	}
	
	//外裁剪
	public DoubleLinkedList[] getExternalPaint(DoubleLinkedList Da,DoubleLinkedList Db,Point b[]){
		int i=0;
		DoubleLinkedList res[]=new DoubleLinkedList[100];
		while(Is_OutPoint_BeTracked_Complete(Da,b)!=null){
			int j=0;
			res[i]=new DoubleLinkedList();
			Node first=Is_OutPoint_BeTracked_Complete(Da,b);
			first.IsBeTracked=1;
			res[i].add(first.data, first.IsIntersection, first.IsBeTracked);
			j++;
			int place=0;       //用于标记现在在哪个边框上跟踪点，0表示被裁剪多边形上，1表示裁剪多边形上 
			Node pcur=first.next;
			res[i].add(j, pcur.data, pcur.IsIntersection, pcur.IsBeTracked);
			pcur.IsBeTracked=1;
			j++;
			//System.out.println("External");
			//System.out.println(first.data.x+" "+first.data.y+" "+first.IsIntersection+" "+first.IsBeTracked);
			//System.out.println(pcur.data.x+" "+pcur.data.y+" "+pcur.IsIntersection+" "+pcur.IsBeTracked);
			while((pcur.data.x!=first.data.x)||(pcur.data.y!=first.data.y)){
				if(pcur.IsIntersection==1){
					if(place==0){
						if(Db.getNode(pcur.data).next.data!=null){
							pcur=Db.getNode(pcur.data).next;
						}
						else{
							pcur=Db.beginMarker.next;
						}
						res[i].add(j, pcur.data, pcur.IsIntersection, pcur.IsBeTracked);
						if(pcur.IsIntersection==1){
							Da.getNode(pcur.data).IsBeTracked=1;
						}
						j++;
						place=1;
					}
					else if(place==1){
						if(Da.getNode(pcur.data).next.data!=null){
							pcur=Da.getNode(pcur.data).next;
						}
						else{
							pcur=Da.beginMarker.next;
						}
						res[i].add(j, pcur.data, pcur.IsIntersection, pcur.IsBeTracked);
						if(pcur.IsIntersection==1){
							Da.getNode(pcur.data).IsBeTracked=1;
						}
						j++;
						place=0;
					}
				}
				else{
					if(place==0){
						if(pcur.next.data!=null){
							pcur=pcur.next;
						}
						else{
							pcur=Da.beginMarker.next;
						}
					}
					else{
						if(pcur.next.data!=null){
							pcur=pcur.next;
						}
						else{
							pcur=Db.beginMarker.next;
						}
					}
					res[i].add(j, pcur.data, pcur.IsIntersection, pcur.IsBeTracked);
					if(pcur.IsIntersection==1){
						Da.getNode(pcur.data).IsBeTracked=1;
					}
					j++;
				}
				//System.out.println(pcur.data.x+" "+pcur.data.y+" "+pcur.IsIntersection+" "+pcur.IsBeTracked);
			}
			i++;
			//System.out.println(i);
		}
		res_length=i;
		return res;
	}
	
	//得到切割与被切割的两条双向链表 
	public DoubleLinkedList[] getDList(Point a[],Point b[])   //a为被切割图形顶点数组，b为切割图形顶点数组 
	{
		DoubleLinkedList Da=new DoubleLinkedList();
		DoubleLinkedList Db=new DoubleLinkedList();
	    int a_len=a.length;
	    int b_len=b.length;
	    //将被裁剪多边形顶点插入链表 
	    for(int i=0;i<a_len;i++){
	    	Da.add(a[i],0,0);
		}
		//将被裁剪多边形首顶点再插入链表尾部
    	Da.add(a[0],0,0);
		//将裁剪多边形顶点插入链表
		for(int i=0;i<b_len;i++){
			Db.add(b[i],0,0);
		}
		//将裁剪多边形首顶点再插入链表尾部
    	Db.add(b[0],0,0);
		//将交点插入链表
		for(int i=0;i<a_len-1;i++) {
			for(int j=0;j<b_len-1;j++){
				if(sideIntersectSide(a[i],a[i+1],b[j],b[j+1])==1){
					Point newP1=getIntersection(a[i],a[i+1],b[j],b[j+1]);
					Point newP2=getIntersection(a[i],a[i+1],b[j],b[j+1]);
					Da.add(Da.getLocation(a[i])+1, newP1, 1, 0);
					Db.add(Db.getLocation(b[j])+1, newP2, 1, 0);
				}
			}
		}
		
		//将被裁剪多边形首尾顶点连线中的交点插入链表
		for(int i=0;i<b_len-1;i++){
			if(sideIntersectSide(a[a_len-1],a[0],b[i],b[i+1])==1){
				Point newP1=getIntersection(a[a_len-1],a[0],b[i],b[i+1]);
				Point newP2=getIntersection(a[a_len-1],a[0],b[i],b[i+1]);
				Da.add(Da.getLocation(a[a_len-1])+1, newP1, 1, 0);
				Db.add(Db.getLocation(b[i])+1, newP2, 1, 0);
			}
		}
		
		//将裁剪多边形首尾顶点连线中的交点插入链表
		for(int i=0;i<a_len-1;i++){
			if(sideIntersectSide(b[b_len-1],b[0],a[i],a[i+1])==1){
				Point newP1=getIntersection(b[b_len-1],b[0],a[i],a[i+1]);
				Point newP2=getIntersection(b[b_len-1],b[0],a[i],a[i+1]);
				Da.add(Da.getLocation(a[i])+1, newP1, 1, 0);
				Db.add(Db.getLocation(b[b_len-1])+1, newP2, 1, 0);
			}
		}
		//将被裁剪多边形首尾顶点连线与裁剪多边形首尾顶点连线中的交点插入链表
		if(sideIntersectSide(a[a_len-1],a[0],b[b_len-1],b[0])==1){
			Point newP1=getIntersection(a[a_len-1],a[0],b[b_len-1],b[0]);
			Point newP2=getIntersection(a[a_len-1],a[0],b[b_len-1],b[0]);
			Da.add(Da.getLocation(a[a_len-1])+1, newP1, 1, 0);
			Db.add(Db.getLocation(b[b_len-1])+1, newP2, 1, 0);
		}
		
		//将被裁剪多边形链表中的每两个顶点之间的交点进行排序
		Node node=Da.beginMarker.next;
		Node nodei=null;
		Node nodej=null;
		while(node.next.data!=null){
			if((node.IsIntersection==0)&&(node.next.IsIntersection==1)){
				for(nodei=node.next;nodei.IsIntersection!=0;nodei=nodei.next){
					for(nodej=nodei.next;nodej.IsIntersection!=0;nodej=nodej.next){
						if(Length_TwoPoint(nodei.data,node.data)>Length_TwoPoint(nodej.data,node.data)){
							int m=nodei.data.x;
							int n=nodei.data.y;
							nodei.data.x=nodej.data.x;
							nodei.data.y=nodej.data.y;
							nodej.data.x=m;
							nodej.data.y=n;
						}
					}
				}
			}
			node=node.next;
		}
		
		//将裁剪多边形链表中的每两个顶点之间的交点进行排序 
		node=Db.beginMarker.next;
		while(node.next.data!=null){
			if((node.IsIntersection==0)&&(node.next.IsIntersection==1)){
				for(nodei=node.next;nodei.IsIntersection!=0;nodei=nodei.next){
					for(nodej=nodei.next;nodej.IsIntersection!=0;nodej=nodej.next){
						if(Length_TwoPoint(nodei.data,node.data)>Length_TwoPoint(nodej.data,node.data)){
							int m=nodei.data.x;
							int n=nodei.data.y;
							nodei.data.x=nodej.data.x;
							nodei.data.y=nodej.data.y;
							nodej.data.x=m;
							nodej.data.y=n;
						}
					}
				}
			}
			node=node.next;
		}
		
		DoubleLinkedList res[]=new DoubleLinkedList[2];
		res[0]=Da;
		res[1]=Db;
		return res;
	}
	
}
