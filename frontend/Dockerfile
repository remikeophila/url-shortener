FROM node:18 as build
WORKDIR /app
COPY ./ /app
RUN npm install
RUN npm run build

FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/url-shortener-ui/browser/ /usr/share/nginx/html
EXPOSE 4200