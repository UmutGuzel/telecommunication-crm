# System Metrics Dashboard for Microservices

This dashboard provides CPU, memory, network traffic, and request metrics for all microservices in the telecommunication-crm system.

## Features

- Visual gauge display showing real-time CPU usage for core services
- Consolidated summary tables showing CPU and memory usage for all services with proper color-coding
- Graph visualization of total user requests across all services
- Request distribution visualization by service and status code 
- Top endpoints table to identify the most frequently accessed resources
- Real-time CPU, memory, and request rate trend graphs for all services
- Individual tables for each service showing detailed CPU metrics
- Real-time updates (every 5 seconds)
- Color-coded thresholds for easy monitoring:
  - CPU: green < 50%, yellow 50-80%, red > 80%
  - Memory: green < 70%, yellow 70-85%, red > 85%
  - Response Time: green < 0.1s, yellow 0.1-0.5s, red > 0.5s
- Both system and process CPU usage metrics
- JVM heap memory usage metrics with percentage calculation

## How to Import

1. Access your Grafana instance at http://localhost:3000
2. Log in with the credentials (default: admin/admin)
3. Navigate to Dashboards > Import
4. Either:
   - Upload the `cpu-metrics-dashboard.json` file, OR
   - Copy and paste the contents of the `cpu-metrics-dashboard.json` file into the JSON field
5. Click "Load"
6. Select your Prometheus data source (should be auto-detected)
7. Click "Import"

## Dashboard Structure

The dashboard is organized into seven main sections:

### 1. Gauge Visualization
- Real-time gauges showing CPU usage for core services (config-server, discovery-server, gateway-server, user-service, and customer-service)
- Immediately shows status of critical infrastructure components with intuitive color coding

### 2. CPU Summary Table 
- Consolidated CPU usage table for all services with proper color-coded values
- Services are sorted by CPU usage (highest at top)
- Color ranges: green (< 50%), yellow (50-80%), red (> 80%)

### 3. Memory Summary Table
- Shows memory usage for all services with absolute values (bytes) and percentage
- Includes max memory allocation and current usage
- Color-coded percentages to highlight memory pressure
- Services sorted by memory usage percentage (highest at top)

### 4. Request Metrics Visualizations
- Graph showing total user requests across all services with stacked areas by service
- Includes "Total Requests" sum line to see aggregate traffic
- Pie chart showing request distribution by service (which services handle the most requests)
- Pie chart showing status code distribution (2xx, 4xx, 5xx) to help identify errors
- Table of top endpoints by request count, sorted by highest traffic

### 5. Trend Graphs
- CPU usage trend graph showing historical data for all services
- Memory usage trend graph showing historical heap usage for all services
- All include mean, max, and last values in a table format

### 6. Detailed Service Tables
The dashboard contains individual table panels for all 11 services:
- Config Server
- Discovery Server
- Gateway Server
- User Service
- Customer Service
- Contract Service
- Plan Service
- Payment Service
- Customer Support Service
- Analytics Service
- Notification Service

Each table shows both `system_cpu_usage` and `process_cpu_usage` metrics.

## Troubleshooting

If metrics don't appear:
1. Verify Prometheus is running and collecting metrics
2. Check that your services are exposing metrics at `/actuator/prometheus`
3. Confirm the job names in Prometheus configuration match those used in the dashboard
4. Check the Prometheus data source configuration in Grafana

If color thresholds are not working:
1. Check that numerical values are correctly identified (not strings)
2. Verify the threshold settings in panel options
3. Ensure display mode is set to "color-background" not "color-background-solid"
4. For memory metrics, make sure the heap area is properly set

If request metrics are missing:
1. Verify your Spring Boot services have the actuator and prometheus dependencies
2. Check that HTTP metrics collection is enabled in your application properties
3. Ensure the `http_server_requests_seconds_count` metric is being exposed
4. Try using different time ranges (5m, 15m, 1h) if traffic is low

## Customization

You can modify the dashboard by:
- Adjusting thresholds in the panel settings
- Adding additional metrics (non-heap memory, garbage collection, network I/O, etc.)
- Changing the refresh rate
- Modifying the panel layouts
- Adding more services to the gauge visualization
- Adding service-specific API endpoint metrics 