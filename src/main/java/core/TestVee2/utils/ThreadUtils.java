package core.TestVee2.utils;

import core.TestVee2.Page;

/**
 * @author icernopolc Utility class for Webdriver to handle thread executions
 */
public class ThreadUtils implements Runnable
{

    private final Thread  t;
    private int     Timer;
    private boolean alive;

    private final String  userT;
    private final String  passwordT;
    private final int     TimeoutT;

    public boolean  dialogHandled;

    public ThreadUtils(String user, String password, int Timeout)
    {
        alive = true;
        dialogHandled = false;
        userT = user;
        TimeoutT = Timeout;
        passwordT = password;
        Timer = 0;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run()
    {
        while (alive && Timer <= TimeoutT && !SeleniumUtils.handleAuthenticationDialog(userT, passwordT))
        {
            Timer++;
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
            }
        }
        if (Timer > TimeoutT)
        {
            stop();
            Page.logger.debug("Timeout Exception, no authentication dialog appeared");
        }
        else
        {
            dialogHandled = true;
        }
    }

    public void stop()
    {
        alive = false;
    }
}
