/* Name: Nabeel Majid(C3287060)
 * Seng4500-Assignment1
 * Date 22/08/23
description: Server that generate linkedlist to store taxscale data and connected to Client to for calculate tax.
*/
public class Node <T>{
    private Node<T> next;
    private Node<T> prev;
    private T data;
    //constructor
    Node ()
    {
        next=null;
        prev=null;
        data=null;

    }
    //constructor
    Node ( T data)
    {
        next=null;
        prev=null;
        this.data=data;

    }
    //constructor
    Node ( T data, Node<T> prev, Node<T> next)
    {
        this.next=next ;
        this.prev=prev;
        this.data=data;

    }
    public void set_Next(Node<T> next)
    {
        this.next= next;
    }

    public void set_Prev(Node<T> prev)
    {
        this.prev= prev;
    }
    public void set_Data(T data)
    {
        this.data= data;
    }

    public Node<T> get_Next()
    {
        return next;
    }


    public Node<T> get_Prev()
    {
        return prev;
    }

    public T get_Data()
    {
        return data;
    }
}
