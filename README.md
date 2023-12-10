# Тестовое задание для Java Backend разработчика

## Тех. стек:

- Java 17+;
- Spring Boot 3+;
- PostgreSQL 14+;
- Hibernate;
- Maven/Gradle (любой на выбор);
- Docker.

Написать WEB-приложение с использованием Spring Boot + Spring Web и Spring Data JPA (Hibernate + PostgreSQL) + Docker.

Приложение должно выполнять следующее:

- должно быть интегрировано с [Nist CPE API](https://nvd.nist.gov/developers/products) (конкретно только с CPE API) посредством REST;
- для указанной интеграции, приложение должно выгружать полностью БД Nist CPE, и сохранять её в локальной БД;
- для указанной интеграции, приложение должно ежедневно, в определённое время суток, обновлять скачанную БД Nist CPE – 
должна быть реализована логика, которая будет обновлять в локальной БД только те записи, которые были обновлены/добавлены в удалённой БД;
- в приложении должен быть реализован REST-контроллер, позволяющий читать полученную из Nist CPE БД информацию. Операции чтения:
- чтение одной записи;
- чтение нескольких записей по входным параметрам: список идентификаторов или наименований;
- чтение N записей с пагинацией + фильтрация по отдельным полям:
  - наименование (частичное совпадение);
  - описание (частичное совпадение);

Приложение должно запускаться с помощью Docker.

Проект должен быть опубликован в Github/Gitlab (на выбор) репозитории, также обязательно должна быть инструкция по запуску проекта.

## Запуск приложения 
Приложение запускается с помощью Docker.

В корне проекта содержатся Dockerfile-ы для postgresql и spring-приложения, а также docker-compose.yml, который запускает
контейнеры spring-boot приложения и базы данных, в которой локально хранятся данные приложения

Для запуска приложения необходимо выполнить команды 
```bash
docker-compose build
docker-compose up
```

Данные команды запустят приложение, и после запуска начнется копирование данных их базы данных Nist CPE API в локальную БД.

База данных Nist CPE API содержит более 1млн записей, поэтому процесс копирования занимае, как правило, более 10 минут

Приложение запускается на порту 8081
## API Endpoints

Чтение одной записи 

```bash
curl --location 'http://localhost:8081/api/cpe?cpeNameId=87316812-5F2C-4286-94FE-CC98B9EAEF53'
```
Параметры:
cpeNameId (не чувствителен к регистру) - id конкретного CPE.

Формат ответа

```json
{
    "deprecated": false,
    "cpeName": "cpe:2.3:a:microsoft:access:-:*:*:*:*:*:*:*",
    "cpeNameId": "87316812-5f2c-4286-94fe-cc98b9eaef53",
    "lastModified": "2011-01-12T14:35:56.427+00:00",
    "created": "2007-08-23T21:05:57.937+00:00",
    "titles": [
        {
            "title": "Microsoft Access",
            "lang": "en"
        },
        {
            "title": "マイクロソフト Access",
            "lang": "ja"
        }
    ]
}
```

Чтение нескольких записей по входным параметрам: список
идентификаторов или наименований

Получение по списку идентификаторов:
```bash
curl --location 'http://localhost:8081/api/cpe/list-by-id?ids={id1},{id2},{id3}'
```
Параметры:
ids (не чувствителен к регистру) - список идентификаторов {id} через запятую.

Формат ответа
```json
[
    {
        "deprecated": true,
        "cpeName": "cpe:2.3:a:matt_johnston:dropbear_ssh_server:0.28:*:*:*:*:*:*:*",
        "cpeNameId": "f47401b1-69d9-4d1a-9c57-245ddd16e668",
        "lastModified": "2018-09-20T11:53:06.007+00:00",
        "created": "2007-08-23T21:16:59.567+00:00",
        "titles": [
            {
                "title": "Matt Johnston Dropbear SSH Server 0.28",
                "lang": "en"
            }
        ],
        "refs": [
            {
                "ref": "http://matt.ucc.asn.au/dropbear/dropbear.html",
                "type": null
            },
            {
                "ref": "https://matt.ucc.asn.au/dropbear/CHANGES",
                "type": "Change Log"
            }
        ],
        "deprecatedBy": [
            {
                "cpeName": "cpe:2.3:a:dropbear_ssh_project:dropbear_ssh:0.28:*:*:*:*:*:*:*",
                "cpeNameId": "f46a7ee4-474d-4810-acd4-65611e62b699"
            }
        ]
    },
    {
        "deprecated": true,
        "cpeName": "cpe:2.3:a:matt_johnston:dropbear_ssh_server:0.29:*:*:*:*:*:*:*",
        "cpeNameId": "4bcd1931-f27b-4b93-9833-e565d237ae07",
        "lastModified": "2018-09-20T11:53:06.040+00:00",
        "created": "2007-08-23T21:16:59.567+00:00",
        "titles": [
            {
                "title": "Matt Johnston Dropbear SSH Server 0.29",
                "lang": "en"
            }
        ],
        "refs": [
            {
                "ref": "https://matt.ucc.asn.au/dropbear/CHANGES",
                "type": "Change Log"
            }
        ],
        "deprecatedBy": [
            {
                "cpeName": "cpe:2.3:a:dropbear_ssh_project:dropbear_ssh:0.29:*:*:*:*:*:*:*",
                "cpeNameId": "41e99ca9-2c98-49d5-aefe-413a1093ed4d"
            }
        ]
    }
]
```

Получение по списку имен:
```bash
curl --location 'http://localhost:8081/api/cpe/list-by-name?names=cpe%3A2.3%3Ao%3Alinux%3Alinux_kernel%3A2.6.0%3A-%3A*%3A*%3A*%3A*%3A*%3A*%2Ccpe%3A2.3%3Ao%3Alinux%3Alinux_kernel%3A2.6.0%3A*%3A*%3A*%3A*%3A*%3A*%3A*'
```

Формат ответа:
```json
[
  {
    "deprecated": false,
    "cpeName": "cpe:2.3:a:3com:3cdaemon:-:*:*:*:*:*:*:*",
    "cpeNameId": "bae41d20-d4af-4af0-aa7d-3bd04da402a7",
    "lastModified": "2011-01-12T14:35:43.723+00:00",
    "created": "2007-08-23T21:05:57.937+00:00",
    "titles": [
      {
        "title": "3Com 3CDaemon",
        "lang": "en"
      },
      {
        "title": "スリーコム 3CDaemon",
        "lang": "ja"
      }
    ]
  },
  {
    "deprecated": false,
    "cpeName": "cpe:2.3:h:3com:tippingpoint_200e:-:*:*:*:*:*:*:*",
    "cpeNameId": "1bd26339-50e7-4279-9ce6-3fde487d7e7a",
    "lastModified": "2011-01-12T14:36:08.973+00:00",
    "created": "2007-08-23T21:05:57.937+00:00",
    "titles": [
      {
        "title": "スリーコム TippingPoint IMS 200E",
        "lang": "ja"
      },
      {
        "title": "3Com TippingPoint IMS 200E",
        "lang": "en"
      }
    ]
  }
]
```

Чтение N записей с пагинацией + фильтрация по отдельным
полям:
- наименование (частичное совпадение);
- описание (частичное совпадение);
```bash
curl --location 'http://localhost:8080/api/cpe/search?page=0&size=10&cpeName=linux&description=github'
```
Параметры:
- page - номер страницы (с 0)
- size - размер страницы 
- cpeName (необязательный) - имя CPE для поиска по частичному совпадению в имени
- description (необязательный) - слово для поиска в описании (в titles и refs)

Формат ответа:

```json
{
  "content": [
    {
      "deprecated": true,
      "cpeName": "cpe:2.3:o:linux:linux_kernel:2.2.13:*:*:*:*:*:*:*",
      "cpeNameId": "dd3795ab-5eab-4200-96b9-4f80e854a16a",
      "lastModified": "2021-06-01T14:14:47.707+00:00",
      "created": "2007-08-23T21:16:59.567+00:00",
      "titles": [
        {
          "title": "Linux Kernel 2.2.13",
          "lang": "en"
        }
      ],
      "refs": [
        {
          "ref": "https://github.com/torvalds/linux",
          "type": "Version"
        }
      ],
      "deprecatedBy": [
        {
          "cpeName": "cpe:2.3:o:linux:linux_kernel:2.2.13:-:*:*:*:*:*:*",
          "cpeNameId": "f4a65157-42c4-467a-ab6c-a92e272a28d3"
        }
      ]
    },
    {
      "deprecated": true,
      "cpeName": "cpe:2.3:o:linux:linux_kernel:2.2.15:*:*:*:*:*:*:*",
      "cpeNameId": "d153944e-efec-45d3-80bf-859bc60ba635",
      "lastModified": "2021-06-01T14:14:47.707+00:00",
      "created": "2007-08-23T21:16:59.567+00:00",
      "titles": [
        {
          "title": "Linux Kernel 2.2.15",
          "lang": "en"
        }
      ],
      "refs": [
        {
          "ref": "https://github.com/torvalds/linux",
          "type": "Version"
        }
      ],
      "deprecatedBy": [
        {
          "cpeName": "cpe:2.3:o:linux:linux_kernel:2.2.15:-:*:*:*:*:*:*",
          "cpeNameId": "0b810e74-c3ef-4409-8068-b082386f06e4"
        }
      ]
    },
    {
      "deprecated": true,
      "cpeName": "cpe:2.3:o:linux:linux_kernel:2.2.16:*:*:*:*:*:*:*",
      "cpeNameId": "5d45f05d-e4cf-451f-aff6-34faba76c823",
      "lastModified": "2021-06-01T14:14:47.707+00:00",
      "created": "2007-08-23T21:16:59.567+00:00",
      "titles": [
        {
          "title": "Linux Kernel 2.2.16",
          "lang": "en"
        }
      ],
      "refs": [
        {
          "ref": "https://github.com/torvalds/linux",
          "type": "Version"
        }
      ],
      "deprecatedBy": [
        {
          "cpeName": "cpe:2.3:o:linux:linux_kernel:2.2.16:-:*:*:*:*:*:*",
          "cpeNameId": "2e448deb-ce6b-474b-9f22-b9cec1171e3e"
        }
      ]
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 3,
    "sort": {
      "empty": true,
      "unsorted": true,
      "sorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 26,
  "totalElements": 77,
  "last": false,
  "size": 3,
  "number": 0,
  "sort": {
    "empty": true,
    "unsorted": true,
    "sorted": false
  },
  "numberOfElements": 3,
  "first": true,
  "empty": false
}
```


