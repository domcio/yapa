from spider import AdCrawler

__author__ = 'Dominik'

from twisted.internet import reactor
from scrapy.crawler import Crawler
from scrapy import signals
from scrapy.utils.project import get_project_settings

import sys
import ast

if len(sys.argv) < 3:
    print 'python main.py <website> <object_as_a_dict>'
    sys.exit(-1)
website = sys.argv[1]
joined_ad = " ".join(sys.argv[2:])
print joined_ad
ad = ast.literal_eval(joined_ad)
print website, ad

spider = AdCrawler(domain=website, ad=ad, allowed_domains=website)
settings = get_project_settings()
crawler = Crawler(settings)
crawler.signals.connect(reactor.stop, signal=signals.spider_closed)
crawler.configure()
# log.start()
crawler.crawl(spider)
crawler.start()
reactor.run()  # the script will block here until the spider_closed signal was sent