package rocklike.netty.messenger.common;

public class IntHolder{
	private int _i;
	
	public IntHolder(int i) {
		this._i = i;
	}
	
	public int get(){
		return _i;
	}
	
	public int getAndIncrease(){
		int old = this._i;
		this._i ++;
		return old;
	}
	public IntHolder set(int i){
		this._i = i;
		return this;
	}
	public IntHolder increase(){
		this._i++;
		return this;
	}
}