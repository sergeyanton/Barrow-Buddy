package nz.ac.canterbury.team1000.gardenersgrove.service;

public class TokenBucketService {

    private int tokens;
    private final int maxRequests;
    private final int rateLimitDuration; // MILLISECONDS
    private final int bucketSize;
    private long nextRefillTime;

    public TokenBucketService(int bucketSize, int maxRequests, int rateLimitDuration) {
        this.bucketSize = bucketSize;
        this.maxRequests = maxRequests;
        this.rateLimitDuration = rateLimitDuration;
        this.refillBucket();
    }

    public boolean consumeToken() {
        refillBucket();
        if (this.tokens > 0) {
            this.tokens--;
            return true;
        }
        return false;
    }

    public void refillBucket() {
        if (System.currentTimeMillis() < this.nextRefillTime) {
            return;
        }
        this.nextRefillTime = System.currentTimeMillis() + this.rateLimitDuration;
        this.tokens = Math.min(this.bucketSize, this.tokens + this.maxRequests);
    }
}
