FROM node:14-alpine
WORKDIR /app
COPY package.json ./
RUN apk update && apk add openjdk11
RUN npm install
COPY . ./
EXPOSE 5394
CMD ["npm", "start"]