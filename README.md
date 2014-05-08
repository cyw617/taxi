AnyTaxi
====
Read our wiki page for everything: <url>https://github.com/cyw617/taxi/wiki</url>


Our product presentation slides
---
<url>http://prezi.com/wexodsbaefpt/comp3111h-anytaxi-product/</url>


Our product demo videos
---
1. Broadcast request, Address autofill in, GCM service, Decline Response, and more. <url>https://www.youtube.com/watch?v=bRtXU1lam48</url>
2. Avoid Driver race condition, Accept Response, and more. <url>https://www.youtube.com/watch?v=InPjygNuhUU</url>
3. Sign up, Phone number validation checking, Sign out, and more. <url>https://www.youtube.com/watch?v=X52mwksOxsY</url>
4. **Main Demo**: AnyTaxi, Driver Location periodical updates, GCM service, Map service, and more. <url>https://www.youtube.com/watch?v=OJ-Eq5rD4Vs</url>


How to build (and test coverage report)
---
First download the project, uncompress it, and put it into a directory.

<code>cd</code> into this directory, and the <code>TaxiClient</code> subdir.

Input these command:
<pre><code>
android update project -p appcompat_v7
android update project -p google-play-services_lib
android update project -p Customer
android update test-project -p AnyTaxi_Customer_Test -m ../Customer
cd AnyTaxi_Customer_Test
ant clean emma debug install test
</code></pre>

And the coverage report is available in
<code>AnyTaxi_Customer_Test/bin/coverage.html</code>

Remember to have your phone/emulator on!
