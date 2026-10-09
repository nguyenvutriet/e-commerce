
const Status = ({text, icon:Icon, bg, color}) => {
    return (
        <div>
            {text} <Icon className={`${bg} ${color} px-2 py-2 font-medium rounded flex items-center gap-1`} size={15} />
        </div>
    )
};

export default Status;

