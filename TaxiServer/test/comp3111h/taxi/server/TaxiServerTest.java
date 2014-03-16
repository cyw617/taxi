package comp3111h.taxi.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mortbay.log.Log;

public class TaxiServerTest {
	
	CloseableHttpClient httpClient;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		String URL = "http://localhost:8888/_ah/login";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email","byronyi@qq.com"));
		params.add(new BasicNameValuePair("continue", "/"));
		params.add(new BasicNameValuePair("action", "Log In"));

		try {
			HttpEntity reqHttpEntity = new UrlEncodedFormEntity(params);
			HttpPost httpPost = new HttpPost(URL);
			httpPost.setEntity(reqHttpEntity);
			httpClient = HttpClients.createDefault();
			httpClient.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@After
	public void tearDown() throws Exception {

		httpClient.close();
	}
	
	@Test
	public void test() {
		String URL = "http://localhost:8888";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		NameValuePair pair1 = new BasicNameValuePair("location-x","128");
		NameValuePair pair2 = new BasicNameValuePair("location-y", "96");
		params.add(pair1);
		params.add(pair2);
		try {
			HttpEntity reqHttpEntity = new UrlEncodedFormEntity(params);
			HttpPost httpPost = new HttpPost(URL);
			httpPost.setEntity(reqHttpEntity);
			CloseableHttpResponse resp = httpClient.execute(httpPost);
			showResponseResult(resp);
			EntityUtils.consume(reqHttpEntity);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	
	private void showResponseResult(HttpResponse resp) throws IllegalStateException, IOException {
		if (null == resp)
			return;
		
		HttpEntity httpEntity = resp.getEntity();
		InputStream inputStream = httpEntity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
													inputStream));
		String result = "";
		String line = "";
		while (null != (line = reader.readLine())) {
			result += line;
		}
		Log.info(result);
	}

}
