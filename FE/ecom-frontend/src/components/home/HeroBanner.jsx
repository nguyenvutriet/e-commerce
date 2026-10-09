
// Import Swiper React components
import { Swiper, SwiperSlide } from 'swiper/react';

// Import Swiper styles
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import { Navigation, Autoplay, Pagination } from 'swiper/modules';
import { bannerLists } from '../../utils';
import { Link } from 'react-router-dom';

const HeroBanner = () => {

    return (
        <div className="rounded-xl overflow-hidden shadow-md">
            <Swiper
                grabCursor={true}
                autoplay={{
                    delay: 4000,
                    disableOnInteraction: false,
                }}
                navigation={true}
                modules={[Pagination, Navigation, Autoplay]}
                pagination={{ clickable: true }}
                slidesPerView={1}
                loop={true}
            >
                {bannerLists.map((item) => (
                    <SwiperSlide key={item.id}>
                        <div
                            className="flex flex-row items-center justify-between min-h-[280px] sm:min-h-[320px] lg:min-h-[360px] px-8 sm:px-14 lg:px-20 pb-10 pt-8"
                            style={{ backgroundColor: item.bg }}
                        >
                            {/* Left: Text Content */}
                            <div className="flex flex-col items-start justify-center gap-3 z-10 max-w-xs sm:max-w-sm lg:max-w-md">
                                <p className="text-white text-sm font-semibold tracking-widest uppercase">
                                    {item.title}
                                </p>
                                <h1 className="text-white text-3xl sm:text-4xl lg:text-5xl font-bold leading-tight">
                                    {item.subtitle}
                                </h1>
                                <p className="text-white text-sm sm:text-base opacity-90">
                                    {item.description}
                                </p>
                                <Link
                                    to="/products"
                                    className="mt-2 inline-block bg-black text-white text-sm font-semibold py-2 px-6 rounded hover:bg-gray-800 transition-colors duration-200"
                                >
                                    Shop
                                </Link>
                            </div>

                            {/* Right: Image */}
                            <div className="flex items-end justify-center flex-shrink-0 ml-4">
                                <img
                                    src={item.image}
                                    alt={item.subtitle}
                                    className="h-[220px] sm:h-[270px] lg:h-[320px] object-contain drop-shadow-lg"
                                />
                            </div>
                        </div>
                    </SwiperSlide>
                ))}
            </Swiper>

            {/* Custom Swiper Styles */}
            <style>{`
                .swiper-button-prev,
                .swiper-button-next {
                    color: white !important;
                    background: rgba(0,0,0,0.25);
                    width: 38px !important;
                    height: 38px !important;
                    border-radius: 50%;
                    transition: background 0.2s;
                }
                .swiper-button-prev:hover,
                .swiper-button-next:hover {
                    background: rgba(0,0,0,0.45);
                }
                .swiper-button-prev::after,
                .swiper-button-next::after {
                    font-size: 14px !important;
                    font-weight: bold;
                }
                .swiper-pagination-bullet {
                    background: white !important;
                    opacity: 0.6 !important;
                    width: 8px !important;
                    height: 8px !important;
                }
                .swiper-pagination-bullet-active {
                    opacity: 1 !important;
                    background: white !important;
                }
            `}</style>
        </div>
    );
}

export default HeroBanner;