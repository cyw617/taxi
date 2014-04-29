taxi
====
<url>http://en.wikipedia.org/wiki/Geohash</url>
This will be our algorithm to search for drivers nearby.

How to build (and test coverage report)
===
First download the project, uncompress it, and put it into a directory.

<code>cd</code> into this directory, and the <code>TaxiClient</code> subdir.

Input these command:
<pre><code>android update project -p Customer/ -n AnyTaxi_Customer
android update test-project -p AnyTaxi_Customer_Test -m ../Customer
cd AnyTaxi_Customer_Test
ant clean emma build install test
</code></pre>

And the coverage report is available in
<code>AnyTaxi_Customer_Test/bin/coverage.html</code>
