AnyTaxi
====
Read our wiki page for everything: <url>https://github.com/cyw617/taxi/wiki</url>


Our product presentation slides
===
<url>http://prezi.com/wexodsbaefpt/comp3111h-anytaxi-product/</url>


Our product demo videos
===
<url>htt[://www.youtube.com</url>


How to build (and test coverage report)
===
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
