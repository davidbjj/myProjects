package com.cajval.connector.utils;
 
import quickfix.SessionID;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
import ar.com.sba.cajval.fixserver.domain.Transaction;

public class Order implements Cloneable {
	
	private Stock stock = null;
	private String clordId = "";
	private String orderId = "";
    private SessionID sessionID = null;
    
    private double quantity = 0.0;
    private double leavesQuantity = 0.0;
    private double cumQuantity = 0.0;
    
    private int open = 0;
    private int executed = 0;
    private char side ='1';
    private char type ;
    private List<Transaction> transactionList= new ArrayList<Transaction>();
    private Double limit = 0.0;
    private Double stop = 0.0;
    
    private double avgPx = 0.0;
    private boolean rejected = false;
    private boolean canceled = false;
    private boolean isNew = true;
    private String message = "";
    private String ID = "";
    private String originalID = "";
//    private static int nextID = 1;
    private String clientAccount;
    private char status;
    private char execType;
    
    private Double minQuantity;

    private Date orderDate;
    private Character timeInForce;
    private Date goodTillDate;
    
    private Double partialPublish;
    private boolean ownAccount;
    private String commissionData;
	private char priceProtectionScope;
	private String numericOrderID;
	private String currency;
	private String detail;
	private char undefinedField;
    private String orderCapacity;
    private Date expireTime;
    private Double offset;
    private String qtyUpdIndicator;
    private Date TransactTime;
    
    private String workingIndicator;
    
    private Component container;
	private String MDEntryID;
    
    public Order(Stock stock) {
		this.stock = stock;
	}
    
    public Order() {
        stock = new Stock("", "", "");
    }
    
    
    /*
    public Object clone() {
        try {
            Order order = (Order)super.clone();
            order.setOriginalID(getID());
            order.setID(order.generateID());
            return order;
        } catch(CloneNotSupportedException e) {}
        return null;
    }

    public String generateID() {
        return Long.valueOf(System.currentTimeMillis()+(nextID++)).toString();
    }
    */
    public SessionID getSessionID() {
        return sessionID;
    }
    public void setSessionID(SessionID sessionID) {
        this.sessionID = sessionID;
    }
    
    
    public int getOpen() {
        return open;
    }
    public void setOpen(int open) {
        this.open = open;
    }
    public int getExecuted() {
        return executed;
    }
    public void setExecuted(int executed) {
        this.executed = executed;
    }
    
    public Double getLimit() {
        return limit;
    }
    public void setLimit(Double limit) {
        this.limit = limit;
    }
    
    public Double getStop() {
        return stop;
    }
    public void setStop(Double stop) {
        this.stop = stop;
    }
    public void setStop(String stop) {
        if(stop == null || stop.equals("")) {
            this.stop = null;
        } else {
            this.stop = new Double(stop);
        }
    }
    public void setAvgPx(double avgPx) {
        this.avgPx = avgPx;
    }
    public double getAvgPx() {
        return avgPx;
    }
    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }
    public boolean getRejected() {
        return rejected;
    }
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
    public boolean getCanceled() {
        return canceled;
    }
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
    public boolean isNew() {
        return isNew;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getID() {
        return ID;
    }
    public void setOriginalID(String originalID) {
        this.originalID = originalID;
    }
    public String getOriginalID() {
        return originalID;
    }    
	public char getSide() {
		return side;
	}
	public void setSide(char side) {
		this.side = side;
	}
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	
	public String getClientAccount() {
		return clientAccount;
	}
	public void setClientAccount(String clientAccount) {
		this.clientAccount = clientAccount;
	}
	public String getClordId() {
		return clordId;
	}
	public void setClordId(String clordId) {
		this.clordId = clordId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}

	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getBinaryOrderId() {
		Base62Converter converter = new Base62Converter();
		String MDEntryID = this.getMDEntryID().substring(1);
		long decoded = converter.decodeBase62(MDEntryID);
		return String.valueOf(decoded);
	}
	
	public Date getOrderDate() {
		return orderDate;
	}
	
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
	public void addTransaction(Transaction transaction){
		
		this.transactionList.add(transaction);
	}
	@Override
	public String toString() {
		return "Order [stock=" + stock + ", clordId=" + clordId + ", orderId="
				+ orderId + ", quantity=" + quantity + ", side=" + side
				+ ", type=" + type + ", originalID=" + originalID
				+ ", clientAccount=" + clientAccount + "]";
	}
	public Double getMinQuantity() {
		return minQuantity;
	}
	public void setMinQuantity(Double minQuantity) {
		this.minQuantity = minQuantity;
	}
	public Character getTimeInForce() {
		return timeInForce;
	}
	public void setTimeInForce(Character timeInForce) {
		this.timeInForce = timeInForce;
	}
	public Date getGoodTillDate() {
		return goodTillDate;
	}
	public void setGoodTillDate(Date goodTillDate) {
		this.goodTillDate = goodTillDate;
	}
	public Double getPartialPublish() {
		return partialPublish;
	}
	public void setPartialPublish(Double partialPublish) {
		this.partialPublish = partialPublish;
	}
	public double getLeavesQuantity() {
		return leavesQuantity;
	}
	public void setLeavesQuantity(double leavesQuantity) {
		this.leavesQuantity = leavesQuantity;
	}
	public boolean isOwnAccount() {
		return ownAccount;
	}
	public void setOwnAccount(boolean ownAccount) {
		this.ownAccount = ownAccount;
	}
	public List<Transaction> getTransactionList() {
		return transactionList;
	}
	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public char getExecType() {
		return execType;
	}
	public void setExecType(char execType) {
		this.execType = execType;
	}
	public String getCommissionData() {
		return commissionData;
	}
	public void setCommissionData(String commissionData) {
		this.commissionData = commissionData;
	}
	public char getPriceProtectionScope() {
		return priceProtectionScope;
	}
	public void setPriceProtectionScope(char priceProtectionScope) {
		this.priceProtectionScope = priceProtectionScope;
	}
	public String getNumericOrderID() {
		return numericOrderID;
	}
	public void setNumericOrderID(String numericOrderID) {
		this.numericOrderID = numericOrderID;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}

    public boolean isTradeExecType(){
        return this.getExecType() == 'F';
    }

	public char getTradeFlagField() {
		return undefinedField;
	}

	public void setTradeFlagField(char undefinedField) {
		this.undefinedField = undefinedField;
	}

    public String getOrderCapacity() {
        return orderCapacity;
    }

    public void setOrderCapacity(String orderCapacity) {
        this.orderCapacity = orderCapacity;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setOffset(Double offset) {
        this.offset = offset;
    }

    public Double getOffset() {
        return offset;
    }
	public double getCumQuantity() {
		return cumQuantity;
	}
	public void setCumQuantity(double cumQuantity) {
		this.cumQuantity = cumQuantity;
	}

	public String getQtyUpdIndicator() {
		return qtyUpdIndicator;
	}

	public void setQtyUpdIndicator(String quantityModifyIndicator) {
		this.qtyUpdIndicator = quantityModifyIndicator;
	}

	public Date getTransactTime() {
		return TransactTime;
	}

	public void setTransactTime(Date transactTime) {
		TransactTime = transactTime;
	}

	public Component getContainer() {
		return container;
	}

	public void setContainer(Component container) {
		this.container = container;
	}

	public String getWorkingIndicator() {
		return workingIndicator;
	}

	public void setWorkingIndicator(String workingIndicator) {
		this.workingIndicator = workingIndicator;
	}

	public void setMDEntryID(String MDEntryID) {
		this.MDEntryID = MDEntryID;
		
	}

	public String getMDEntryID() {
		return MDEntryID;
	}
	
}

