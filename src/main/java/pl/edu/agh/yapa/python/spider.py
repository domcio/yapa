from collections import defaultdict
from scrapy.contrib.linkextractors.lxmlhtml import LxmlLinkExtractor
from scrapy.contrib.spiders import Rule, CrawlSpider
from scrapy.exceptions import CloseSpider
from scrapy.item import Item
from scrapy.item import DictItem, Field, BaseItem


class FlexItem(Item):
    def __setitem__(self, key, value):
        if key not in self.fields:
            self.fields[key] = Field()

        self._values[key] = value


def create_item_class(class_name, field_list):
    print "creating class for ", field_list
    field_dict = {}
    for field_name in field_list:
        field_dict[field_name] = Field()
    field_dict['url'] = Field()

    return type(class_name, (DictItem,), field_dict)


class Page(Item):
    url = Field()
    title = Field()
    description = Field()


class AdCrawler(CrawlSpider):
    name = 'AdCrawler'
    overall_count = 0
    nonempty_count = 0
    rules = [Rule(LxmlLinkExtractor(), 'parse_ad', follow=True)]

    def __init__(self, **kw):
        super(AdCrawler, self).__init__(**kw)
        url = kw.get('url') or kw.get('domain')
        self.keyword = kw.get('keyword') or ''
        self.start_urls = [url]
        domain = url.replace('http://', '')
        if '/' in domain:
            domain = domain[:domain.find('/')]
        self.allowed_domains = [domain]
        self.ad_dict = kw.get('ad')
        
    def parse_ad(self, response):
    
        self.overall_count += 1
        ad = FlexItem()
        find_positive = self.keyword in response.url
        print 'response url', response.url, '\n'
        ad['url'] = response.url
        empty = ad['url'] == '' or ad['url'] is None
        for k, v in self.ad_dict.iteritems():
            ad[k] = " ".join(map(lambda x: x.strip(), response.xpath(v).extract())).strip()
            if ad[k] == '' or ad[k] is None:
                empty = True
            find_positive = find_positive or self.keyword in ad[k]
            
        if self.nonempty_count == 100 or self.overall_count == 1000:
            print 'page limit reached'
            raise CloseSpider('page limit reached')
            
        if not empty:
            self.nonempty_count += 1
            print ad
            return ad