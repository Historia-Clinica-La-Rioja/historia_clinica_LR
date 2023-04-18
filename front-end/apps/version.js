const fs = require('fs');
const path = require('path');
const child_process = require('child_process');

const branchName = process.env.CI_COMMIT_REF_SLUG || child_process.execSync('git rev-parse --abbrev-ref HEAD').toString().trim();
const commitHash = child_process.execSync('git rev-parse --short HEAD').toString().trim();

const envFile = path.join(__dirname, 'projects/hospital/src/environments/environment.prod.ts');

fs.readFile(envFile, 'utf8', function (err,data) {
  if (err) {
    return console.log(err);
  }
  const result = data.replace(/branchName: '(.*)'/, `branchName: '${branchName}'`);
  const updatedResult = result.replace(/commitHash: '(.*)'/, `commitHash: '${commitHash}'`);

  fs.writeFile(envFile, updatedResult, 'utf8', function (err) {
    if (err) return console.log(err);
  });
});
