// Configure webpack to handle Node.js 'os' module properly
config.resolve = config.resolve || {};
config.resolve.fallback = config.resolve.fallback || {};
config.resolve.fallback.os = false;
