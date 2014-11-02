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
    page_count = 0
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
        print 'allowed domains: ', self.allowed_domains
        self.ad_dict = kw.get('ad')
        # self.rules = [Rule(LxmlLinkExtractor(allow=['.*?' + self.keyword + '.*']), 'parse_ad', follow=True)]

    def parse_ad(self, response):
        ad = FlexItem()

        find_positive = self.keyword in response.url
        print response.url
        ad['url'] = response.url
        for k, v in self.ad_dict.iteritems():
            ad[k] = " ".join(map(lambda x: x.strip(), response.xpath(v).extract())).strip()
            find_positive = find_positive or self.keyword in ad[k]

        # ad['title'] = map(lambda x: x.strip(), response.xpath("//h1[@class='brkword lheight28']/text()").extract())
        #ad['description'] = map(lambda x: x.strip(),
        #                        response.xpath("//p[@class='pding10 lheight20 large']/text()").extract())
        # if (ad['title'] != []) or (ad['description'] != []):
        if self.page_count == 100:
            raise CloseSpider('page limit reached')
        # print ad
        # if find_positive:
        #     print "positive"
        self.page_count += 1
        return ad