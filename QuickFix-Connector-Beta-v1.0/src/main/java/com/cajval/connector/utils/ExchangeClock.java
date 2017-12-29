package com.cajval.connector.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.Timer;


public class ExchangeClock implements Observer {
	JLabel label = null;
	private ClockModel model;
	private long differentialTime;
	
	public ExchangeClock(JLabel jl) {
		model = new ClockModel();
		label = jl;
		label.setVisible(false);
		model.addObserver(this);
	}
	
	public void update (Observable observable, Object obj)
    {
		if (obj instanceof java.util.Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss z  ");
	        label.setText(sdf.format(getRightDate()));
		}
		else {
			if (obj.equals("ON") || obj.equals("RESET_WARN") || obj.equals("WARN")){
				label.setVisible(true);
			}
			else {
				label.setVisible(false);
			}
		}
    }
	
	public void calcDifferentialDate(Date serverDate){
		Date currentDate = new Date();
		
		this.differentialTime = currentDate.getTime() - serverDate.getTime();
		
		DecimalFormat df = new DecimalFormat("#.####");
		this.label.setToolTipText(df.format(0.001 * this.differentialTime) + " Seg");
	}
	 
	
	private Date getRightDate(){
		Date currentDate = new Date();
		return new Date(currentDate.getTime() - this.differentialTime);
	}

 
}

class ClockModel extends Observable
{
    public ClockModel()
    {
        Timer timer = new Timer (1000, new ActionListener ()
        {
            public void actionPerformed(ActionEvent e)
            {
                setChanged();
                notifyObservers (new Date());
            }
        });
        timer.start();
    }
}