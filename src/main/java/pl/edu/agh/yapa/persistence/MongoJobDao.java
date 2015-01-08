package pl.edu.agh.yapa.persistence;

import com.mongodb.*;
import org.bson.types.ObjectId;
import pl.edu.agh.yapa.model.Job;
import pl.edu.agh.yapa.model.MonitoringJob;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piotrek on 08.01.15.
 */
public class MongoJobDao implements JobDao {
    private static final String JOBS_COLLECTION = "AdJobs";

    private final DB database;
    private final AdTemplateDao adTemplateDao;

    public MongoJobDao(DB database, AdTemplateDao adTemplateDao) {
        this.database = database;
        this.adTemplateDao = adTemplateDao;
        database.createCollection(JOBS_COLLECTION, null);
    }

    @Override
    public Job getJobByName(String jobName) throws InvalidDatabaseStateException {
        DBCollection jobs = database.getCollection(JOBS_COLLECTION);
        DBObject finder = new BasicDBObject().append("name", jobName);
        DBCursor cursor = jobs.find(finder);
        DBObject found = cursor.next();//we assume there would be a job with this name
        return jobFromJson(found);
    }

    @Override
    public List<Job> getJobs() throws InvalidDatabaseStateException {
        DBCollection jobsCollection = database.getCollection(JOBS_COLLECTION);
        List<Job> jobs = new ArrayList<>();
        for (DBObject jobJson : jobsCollection.find()) {
            jobs.add(jobFromJson(jobJson));
        }
        return jobs;
    }

    @Override
    public ObjectId insertJob(MonitoringJob job) throws InvalidDatabaseStateException {
        DBCollection coll = database.getCollection(JOBS_COLLECTION);
        BasicDBObject jobObj = new BasicDBObject();
        jobObj.append("name", job.getName());
        jobObj.append("website", job.getWebsite());
        jobObj.append("template", adTemplateDao.insertTemplate(job.getTemplate()));
        coll.insert(jobObj);
        return (ObjectId) jobObj.get("_id");
    }

    private Job jobFromJson(DBObject json) throws InvalidDatabaseStateException {
        MonitoringJob job = new MonitoringJob();
        job.setName((String) json.get("name"));
        // TODO remove cast
        job.setTemplate(((MongoAdTemplateDao) adTemplateDao).getTemplateByID((ObjectId) json.get("template")));
        job.setWebsite((String) json.get("website"));
        return job;
    }
}
