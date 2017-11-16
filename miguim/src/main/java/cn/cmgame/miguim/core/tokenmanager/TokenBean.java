package cn.cmgame.miguim.core.tokenmanager;

/**
 * Created by Amuro on 2017/10/25.
 */

public class TokenBean
{
	private int status;
	private String message;
	private String token;
	private String server;
	private int port;
	private int expire;
	private int heartbeat;
	private int timeout;

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getServer()
	{
		return server;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public int getExpire()
	{
		return expire;
	}

	public void setExpire(int expire)
	{
		this.expire = expire;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public int getHeartbeat()
	{
		return heartbeat;
	}

	public void setHeartbeat(int heartbeat)
	{
		this.heartbeat = heartbeat;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	@Override
	public String toString()
	{
		return "TokenBean{" +
				"status=" + status +
				", message='" + message + '\'' +
				", token='" + token + '\'' +
				", server='" + server + '\'' +
				", port=" + port +
				", expire=" + expire +
				", heartbeat=" + heartbeat +
				", timeout=" + timeout +
				'}';
	}
}
