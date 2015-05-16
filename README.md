# PageRanker

Implement PageRank and run it on the Wiki-Large corpus.
link.srt too large of a file to include



Where Bu is the set of pages that point to u, and Lv is the number of outgoing links from page v (not counting duplicate links)
Dataset

Implement the PageRank algorithm and test it. Analysis will be done on the WikiLarge corpus: http://ciir.cs.umass.edu/cmpsci446/links.srt.gz

Format

Source_Page_Name1 '\t' Target_Page_Name1 '\n'
Source_Page_Name1 '\t' Target_Page_Name2 '\n'
Source_Page_Name2 '\t' Target_Page_Name3 '\n'
...
This might be the largest file you have ever processed. (56MiB compressed, 245 MiB uncompressed). It will be better to scan through it on disk than hold in memory.

Settings

Using lambda=0.15 and tau=0.01
Compute the top 50 pages in terms of having the most inlinks and submit this list of pages with their inlink count
Compute the top 50 pages in terms of PageRank (see Fig. 4.11 in the book). Submit this list of pages with their PageRank.
Explore initialization of PageRank scores and the need for the random-surfer factor.
